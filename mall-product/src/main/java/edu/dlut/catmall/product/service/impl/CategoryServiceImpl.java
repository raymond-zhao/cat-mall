package edu.dlut.catmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlut.catmall.product.dao.CategoryDao;
import edu.dlut.catmall.product.entity.CategoryEntity;
import edu.dlut.catmall.product.service.CategoryBrandRelationService;
import edu.dlut.catmall.product.service.CategoryService;
import edu.dlut.catmall.product.vo.Catelog2VO;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.Query;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 这个类继承了 ServiceImpl
        // 1. 查出所有分类列表
        List<CategoryEntity> entities = baseMapper.selectList(null); // 传入 null 代表查询所有

        // 2. 组装成树形结构
        return entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu -> {
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                }).sorted((m1, m2) -> m1.getSort() == null ? 0 : m1.getSort() - (m2.getSort() == null ? 0 : m2.getSort())).collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查需要删除的菜单是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, path);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[path.size()]);
    }

    /**
     * 级联更新所有关联数据
     *
     * @param category
     */
    @Transactional
//    @Caching(evict = {
//            @CacheEvict(value = "category", key = "'getLevel1Categories'"),
//            @CacheEvict(value = "category", key = "'getCatalogJson'")
//    })
    @CacheEvict(value = "category", allEntries = true)
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    @Cacheable(value = {"category"}, key = "#root.method.name", sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categories() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
    }

    @Cacheable(value = {"category"}, key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2VO>> getCatalogJson() {
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categories = getParent_cid(selectList, 0L);

        return level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            List<Catelog2VO> catelog2VOS = null;
            if (!CollectionUtils.isEmpty(categoryEntities)) {
                catelog2VOS = categoryEntities.stream().map(l2 -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    List<CategoryEntity> level3Catalog = getParent_cid(selectList, l2.getCatId());
                    if (!CollectionUtils.isEmpty(level3Catalog)) {
                        List<Catelog2VO.Catelog3VO> collect = level3Catalog.stream().map(l3 -> {
                            Catelog2VO.Catelog3VO catelog3VO = new Catelog2VO.Catelog3VO(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3VO;
                        }).collect(Collectors.toList());
                        catelog2VO.setCatalog3List(collect);
                    }
                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOS;
        }));
    }

    /**
     * 利用Redis进行缓存商品分类数据
     *
     * @return
     */
    public Map<String, List<Catelog2VO>> getCatalogJson2() throws InterruptedException {
        // TODO 产生堆外内存溢出 OutOfDirectMemoryError
        /**
         * 1. SpringBoot2.0之后默认使用 lettuce 作为操作 redis 的客户端，lettuce 使用 Netty 进行网络通信
         * 2. lettuce 的 bug 导致 Netty 堆外内存溢出 -Xmx300m   Netty 如果没有指定对外内存 默认使用 JVM 设置的参数
         *      可以通过 -Dio.netty.maxDirectMemory 设置堆外内存
         * 解决方案：不能仅仅使用 -Dio.netty.maxDirectMemory 去调大堆外内存
         *      1. 升级 lettuce 客户端   2. 切换使用 jedis
         *
         *      RedisTemplate 对 lettuce 与 jedis 均进行了封装 所以直接使用 详情见：RedisAutoConfiguration 类
         */

        /**
         * - 空结果缓存：解决缓存穿透
         *
         * - 设置过期时间（加随机值）：解决缓存雪崩
         *
         * - 加锁：解决缓存击穿
         */

        // 给缓存中放入JSON字符串，取出JSON字符串还需要逆转为能用的对象类型

        // 1. 加入缓存逻辑， 缓存中存的数据是 JSON 字符串
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            // 2 如果缓存未命中 则查询数据库
            // 4 返回从数据库中查询的数据
            return getCatalogJsonFromDBWithRedisLock();
        }

        return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2VO>>>() {});
    }

    public Map<String, List<Catelog2VO>> getCatalogJsonFromDBWithRedissonLock() {

        RLock lock = redisson.getLock("catalogJson-lock");

        lock.lock();
        Map<String, List<Catelog2VO>> dataFromDB;
        try {
            dataFromDB = getDataFromDB();
        } finally {
            lock.unlock();
        }
        return dataFromDB;
    }

    /**
     * Redis 实现分布式锁
     *
     * @return
     */
    public Map<String, List<Catelog2VO>> getCatalogJsonFromDBWithRedisLock() throws InterruptedException {
        // 1 Redis 占位
        String uuid = UUID.randomUUID().toString();
        Boolean lockResult = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lockResult) {
            // 2 加锁成功 执行业务
            Map<String, List<Catelog2VO>> dataFromDB;
            try {
                dataFromDB = getDataFromDB();
            } finally {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"), uuid);
            }
//            String lockFromRedis = stringRedisTemplate.opsForValue().get("lock");
//            if (uuid.equals(lockFromRedis))
//                stringRedisTemplate.delete("lock"); // 删除锁
            return dataFromDB;
        } else {
            // 3 加锁失败 睡眠 100ms 后重试
            Thread.sleep(100);
            return getCatalogJsonFromDBWithRedisLock();
        }
    }

    private Map<String, List<Catelog2VO>> getDataFromDB() {
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2VO>>>() {
            });
            
        }

        List<CategoryEntity> selectList = baseMapper.selectList(null);
        List<CategoryEntity> level1Categories = getParent_cid(selectList, 0L);

        Map<String, List<Catelog2VO>> parentCid = level1Categories.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            List<Catelog2VO> catelog2VOS = null;
            if (!CollectionUtils.isEmpty(categoryEntities)) {
                catelog2VOS = categoryEntities.stream().map(l2 -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    List<CategoryEntity> level3Catalog = getParent_cid(selectList, l2.getCatId());
                    if (!CollectionUtils.isEmpty(level3Catalog)) {
                        List<Catelog2VO.Catelog3VO> collect = level3Catalog.stream().map(l3 -> new Catelog2VO.Catelog3VO(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName())).collect(Collectors.toList());
                        catelog2VO.setCatalog3List(collect);
                    }
                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOS;
        }));
        // 3 查到的数据再放入缓存 将对象转为JSON放入缓存
        String cache = JSON.toJSONString(parentCid);
        stringRedisTemplate.opsForValue().set("catalogJSON", cache, 1, TimeUnit.DAYS);
        return parentCid;
    }

    /**
     * 从数据库查询并封装商品分类数据
     *
     * @return
     */
    public Map<String, List<Catelog2VO>> getCatalogJsonFromDBWithLocalLock() {
        /**
         * 优化
         * 1. 将数据库的多次查询变为一次查询
         *
         * SpringBoot 所有的组件在容器中默认都是单例的，使用 synchronized (this) 可以实现加锁
         */
        synchronized (this) {
            /**
             * 得到锁之后 应该再去缓存中确定一次，如果没有的话才需要继续查询
             *
             * 假如有100W个并发请求，首先得到锁的请求开始查询，此时其他的请求将会排队等待锁
             * 等到获得锁的时候再去执行查询，但是此时有可能前一个加锁的请求已经查询成功并且将结果添加到了缓存中
             */

            return getDataFromDB();
        }
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parentCid) {
        return selectList.stream().filter(o -> o.getParentCid().equals(parentCid)).collect(Collectors.toList());
    }

    private List<Long> findParentPath(Long catelogId, List<Long> path) {
        // 收集当前结点
        path.add(catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), path);
        }
        return path;
    }

    /**
     * 递归查找所有菜单的子菜单
     *
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, all)))
                .sorted((m1, m2) -> m1.getSort() == null ? 0 : m1.getSort() - (m2.getSort() == null ? 0 : m2.getSort()))
                .collect(Collectors.toList());
    }

}