package edu.dlut.catmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.order.vo.OrderConfirmVO;
import edu.dlut.catmall.order.vo.OrderSubmitVO;
import edu.dlut.catmall.order.vo.SubmitOrderResponseVO;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.order.entity.OrderEntity;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:50:58
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    OrderConfirmVO confirmOrder() throws ExecutionException, InterruptedException;

    SubmitOrderResponseVO submitOrder(OrderSubmitVO submitVO);
}

