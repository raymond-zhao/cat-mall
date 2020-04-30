package edu.dlut.catmall.order.dao;

import edu.dlut.catmall.order.entity.OrderReturnReasonEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退货原因
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:50:57
 */
@Mapper
public interface OrderReturnReasonDao extends BaseMapper<OrderReturnReasonEntity> {
	
}
