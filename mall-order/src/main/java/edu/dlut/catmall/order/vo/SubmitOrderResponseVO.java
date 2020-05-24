package edu.dlut.catmall.order.vo;

import edu.dlut.catmall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  16:02
 * DESCRIPTION:
 **/
@Data
public class SubmitOrderResponseVO {

    private OrderEntity orderEntity;

    private Integer code;

}
