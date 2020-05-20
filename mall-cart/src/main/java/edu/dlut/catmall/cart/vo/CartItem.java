package edu.dlut.catmall.cart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  11:43
 * DESCRIPTION: 购物车里的具体商品项
 **/
public class CartItem {

    private Long skuId;

    private Boolean checked = true;

    private String title;

    private String image;

    private List<String> skuAttrs;

    private BigDecimal price;

    private Integer count;

    private BigDecimal totalPrice;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getSkuAttrs() {
        return skuAttrs;
    }

    public void setSkuAttrs(List<String> skuAttrs) {
        this.skuAttrs = skuAttrs;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal("" + this.count));
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
