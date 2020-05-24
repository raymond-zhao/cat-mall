package edu.dlut.catmall.ware.service.impl;

import edu.dlut.catmall.ware.exception.NoStockException;
import edu.dlut.catmall.ware.feign.ProductFeignService;
import edu.dlut.catmall.ware.vo.OrderItemVO;
import edu.dlut.catmall.ware.vo.SkuHasStockVO;
import edu.dlut.catmall.ware.vo.WareSkuLockVO;
import edu.dlut.common.utils.R;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.Query;

import edu.dlut.catmall.ware.dao.WareSkuDao;
import edu.dlut.catmall.ware.entity.WareSkuEntity;
import edu.dlut.catmall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private WareSkuDao wareSkuDao;

    @Resource
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            queryWrapper.eq("sku_id", skuId);
        }

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断如果还没有这个库存记录新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (entities == null || entities.size() == 0) {
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
            //1、自己catch异常
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            try {
                R info = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");

                if (info.getCode() == 0) {
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }
            wareSkuDao.insert(skuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockVO> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVO> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVO vo = new SkuHasStockVO();
            // 查询当前 sku 的库存
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count == null ? false : count > 0);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 下订单时锁定库存
     *
     * @param vo
     * @return
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVO vo) {
        // 按照下单的收货地址 找到一个就近的仓库 锁定库存

        // 找到每个商品在哪些仓库还有库存
        List<OrderItemVO> locks = vo.getLocks();

        List<SkuWareHasStock> collect = locks.stream().map(o -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = o.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(o.getCount());
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareIds(wareIds);
            return stock;
        }).collect(Collectors.toList());

        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareIds();
            if (CollectionUtils.isEmpty(wareIds))
                throw new NoStockException(skuId);
            for (Long wareId : wareIds) {
                // 成功返回 1 失败返回 0
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if (count == 1) {
                    skuStocked = true;
                    break;
                } else {
                    // 当前仓库锁定库存失败 重试下一个仓库
                }
            }
            if (skuStocked == false)
                throw new NoStockException(skuId);
        }

        return true;
    }

    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }

}