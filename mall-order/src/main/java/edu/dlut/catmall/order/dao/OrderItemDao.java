package edu.dlut.catmall.order.dao;

import edu.dlut.catmall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:50:58
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
