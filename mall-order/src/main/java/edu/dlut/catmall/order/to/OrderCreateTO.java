package edu.dlut.catmall.order.to;

import edu.dlut.catmall.order.entity.OrderEntity;
import edu.dlut.catmall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  17:18
 * DESCRIPTION:
 **/
@Data
public class OrderCreateTO {

    private OrderEntity orderEntity;

    private List<OrderItemEntity> orderItems;

    private BigDecimal payPrice;

    private BigDecimal fare;

}
