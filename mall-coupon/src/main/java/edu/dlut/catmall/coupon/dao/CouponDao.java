package edu.dlut.catmall.coupon.dao;

import edu.dlut.catmall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:39:48
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
