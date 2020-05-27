package edu.dlut.catmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.ware.vo.SkuHasStockVO;
import edu.dlut.catmall.ware.vo.WareSkuLockVO;
import edu.dlut.common.to.mq.OrderTO;
import edu.dlut.common.to.mq.StockLockedTO;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:51:46
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVO> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVO vo);

    void unlockStock(StockLockedTO to);

    void unlockStock(OrderTO orderTO);
}

