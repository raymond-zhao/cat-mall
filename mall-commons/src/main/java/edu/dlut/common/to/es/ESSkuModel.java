package edu.dlut.common.to.es;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/12  18:36
 * DESCRIPTION:
 **/
@Data
@ToString
public class ESSkuModel {

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private Long brandId;

    private String  brandName;

    private String brandImg;

    private Long catalogId;

    private String catalogName;

    private List<Attrs> attrs;

    public static class Attrs {

        private Long attrId;

        private String attrName;

        private String attrValue;

    }

}
