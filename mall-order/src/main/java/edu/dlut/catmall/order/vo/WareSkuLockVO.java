package edu.dlut.catmall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  20:03
 * DESCRIPTION:
 **/
@Data
public class WareSkuLockVO {
    private String orderSn;
    private List<OrderItemVO> locks;
}
