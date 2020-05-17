package edu.dlut.catmall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlut.catmall.product.dao.AttrGroupDao;
import edu.dlut.catmall.product.dao.SkuSaleAttrValueDao;
import edu.dlut.catmall.product.entity.BrandEntity;
import edu.dlut.catmall.product.service.BrandService;
import edu.dlut.catmall.product.service.CategoryBrandRelationService;
import edu.dlut.catmall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Slf4j
class MallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Resource
    private AttrGroupDao attrGroupDao;

    @Resource
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void skuSaleAttrValueDaoTest() {
        skuSaleAttrValueDao.getSaleAttrsBySpuId(16L).forEach(System.out::println);
    }

    @Test
    public void attrGroupDaoTest() {
        attrGroupDao.getAttrGroupWithAttrsBySpuIdAndCatalogId(16L, 225L).forEach(System.out::println);
    }

    @Test
    public void redissonTest() {
        System.out.println(redissonClient);
    }

    @Test
    public void testRedis() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", "world" + UUID.randomUUID().toString());
        System.out.println(ops.get("hello"));
    }

    @Test
    public void testUpdateCascade() {
        categoryBrandRelationService.updateCategory(225L, "手机1");
    }

    @Test
    public void findPath() {
        Long[] path = categoryService.findCatelogPath(225L);
        log.info("path: {}", Arrays.asList(path));
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(System.out::print);
    }

}
