package edu.dlut.catmall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.common.to.SkuReductionTo;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:39:47
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo reductionTo);
}

