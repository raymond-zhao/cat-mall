package edu.dlut.catmall.coupon.dao;

import edu.dlut.catmall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:39:47
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
