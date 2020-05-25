package edu.dlut.common.exception;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  20:46
 * DESCRIPTION:
 **/
public class NoStockException extends RuntimeException {

    private Long skuId;

    public NoStockException(Long skuId) {
        super("商品 ID:" + skuId + "没有足够的库存.");
    }

    public NoStockException(String msg) {
        super(msg);
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
