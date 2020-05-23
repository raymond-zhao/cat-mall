package edu.dlut.catmall.order.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/23  15:07
 * DESCRIPTION:
 **/
public class OrderConfirmVO {

    @Getter
    @Setter
    private List<MemberAddressVO> addresses;

    @Getter
    @Setter
    private List<OrderItemVO> items;

    @Getter
    @Setter
    private String orderToken; // 防重令牌

    // 发票记录

    @Getter
    @Setter
    private Map<Long, Boolean> stocks;

    // 优惠券信息
    @Getter
    @Setter
    private Integer integration;

    public Integer getCount() {
        Integer count = 0;
        if (!CollectionUtils.isEmpty(items)) {
            for (OrderItemVO orderItemVO : items)
                count += orderItemVO.getCount();
        }
        return count;
    }

    // private BigDecimal total; // 订单总额
    public BigDecimal getTotal() {
        BigDecimal sum = new BigDecimal("0");
        if (!CollectionUtils.isEmpty(items)) {
            for (OrderItemVO itemVO : items) {
                BigDecimal multiply = itemVO.getPrice().multiply(new BigDecimal(itemVO.getCount().toString()));
                sum = sum.add(multiply);
            }
        }
        return sum;
    }

    // private BigDecimal payPrice; // 应付价格
    public BigDecimal getPayPrice() {
        return getTotal();
    }

}
