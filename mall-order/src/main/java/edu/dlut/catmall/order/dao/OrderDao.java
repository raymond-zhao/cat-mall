package edu.dlut.catmall.order.dao;

import edu.dlut.catmall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:50:58
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
