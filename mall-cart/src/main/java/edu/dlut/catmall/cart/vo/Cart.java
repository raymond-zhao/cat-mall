package edu.dlut.catmall.cart.vo;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  11:43
 * DESCRIPTION:
 **/
@Data
public class Cart {

    private List<CartItem> items;

    private Integer countNum;

    private Integer countType; // 商品类型数量

    private BigDecimal totalAmount;

    private BigDecimal reduction = new BigDecimal("0.00"); // 减免优惠

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int count = 0;
        if (!CollectionUtils.isEmpty(items)) {
            for (CartItem cartItem : items)
                count += cartItem.getCount();
        }
        return count;
    }

    public Integer getCountType() {
        int count = 0;
        if (!CollectionUtils.isEmpty(items)) {
            for (CartItem cartItem : items)
                count++;
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        // 计算购物项总价
        if (!CollectionUtils.isEmpty(items)) {
            for (CartItem cartItem : items) {
                if (cartItem.getChecked())
                    amount = amount.add(cartItem.getTotalPrice());
            }
        }
        // 计算优惠后的价格
        return amount.subtract(getReduction());
    }

    public BigDecimal getReduction() {
        return reduction;
    }

    public void setReduction(BigDecimal reduction) {
        this.reduction = reduction;
    }
}
