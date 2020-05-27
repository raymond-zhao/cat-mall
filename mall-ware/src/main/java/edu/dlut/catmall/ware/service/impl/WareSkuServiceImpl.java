package edu.dlut.catmall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.rabbitmq.client.Channel;
import edu.dlut.catmall.ware.entity.WareOrderTaskDetailEntity;
import edu.dlut.catmall.ware.entity.WareOrderTaskEntity;
import edu.dlut.catmall.ware.exception.NoStockException;
import edu.dlut.catmall.ware.feign.OrderFeign;
import edu.dlut.catmall.ware.feign.ProductFeignService;
import edu.dlut.catmall.ware.service.WareOrderTaskDetailService;
import edu.dlut.catmall.ware.service.WareOrderTaskService;
import edu.dlut.catmall.ware.vo.OrderItemVO;
import edu.dlut.catmall.ware.vo.OrderVO;
import edu.dlut.catmall.ware.vo.SkuHasStockVO;
import edu.dlut.catmall.ware.vo.WareSkuLockVO;
import edu.dlut.common.to.mq.OrderTO;
import edu.dlut.common.to.mq.StockDetailTO;
import edu.dlut.common.to.mq.StockLockedTO;
import edu.dlut.common.utils.R;
import lombok.Data;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private WareSkuDao wareSkuDao;

    @Resource
    private ProductFeignService productFeignService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private WareOrderTaskService wareOrderTaskService;

    @Resource
    private WareOrderTaskDetailService wareOrderTaskDetailService;

    @Resource
    private OrderFeign orderFeign;

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
            vo.setHasStock(count != null && count > 0);
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

        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
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
                    // 告诉 MQ 库存锁定成功
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity(null, skuId, "", hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    wareOrderTaskDetailService.save(wareOrderTaskDetailEntity);
                    StockLockedTO stockLockedTO = new StockLockedTO();
                    StockDetailTO stockDetailTO = new StockDetailTO();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity, stockDetailTO);
                    stockLockedTO.setId(taskEntity.getId());
                    stockLockedTO.setDetail(stockDetailTO);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTO);
                    break;
                } else {
                    // 当前仓库锁定库存失败 重试下一个仓库
                }
            }
            if (!skuStocked)
                throw new NoStockException(skuId);
        }

        return true;
    }

    @Override
    public void unlockStock(StockLockedTO to) {
        StockDetailTO detail = to.getDetail();
        Long detailId = detail.getId();
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(detailId);
        if (!ObjectUtils.isEmpty(byId)) {
            // 解锁
            Long id = to.getId();
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn();
            R r = orderFeign.getOrderStatus(orderSn);
            if (r.getCode() == 0) {
                OrderVO data = r.getData(new TypeReference<OrderVO>() {});
                if (data.getStatus() == 4 || ObjectUtils.isEmpty(data)) {
                    // 订单已经被取消了 才能解锁库存
                    if (byId.getLockStatus() == 1)
                        unlockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                }
            } else {
                // 消息拒绝以后重新放到队列里面 让别人继续消费解锁
                throw new RuntimeException("远程服务调用失败");
            }
        }
    }

    @Transactional
    @Override
    public void unlockStock(OrderTO orderTO) {
        String orderSn = orderTO.getOrderSn();
        WareOrderTaskEntity task = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = task.getId();
        List<WareOrderTaskDetailEntity> entities = wareOrderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>().eq("task_id", id)
                        .eq("lock_status", 1)
        );
        for (WareOrderTaskDetailEntity entity : entities)
            unlockStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum(), entity.getId());
    }

    private void unlockStock(Long skuId, Long wareId, Integer num, Long detailId) {
        // 库存解锁
        wareSkuDao.unlockStock(skuId, wareId, num);
        WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity();
        wareOrderTaskDetailEntity.setId(detailId);
        wareOrderTaskDetailEntity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(wareOrderTaskDetailEntity);
    }

    @Data
    static class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareIds;
    }

}