package edu.dlut.catmall.search.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/16  09:42
 * DESCRIPTION:
 **/
@Data
@ToString
public class SearchParam {

    private String keyword; // 全文检索关键字

    private Long catalog3Id; // 三级分类ID

    /**
     * 排序条件
     *
     * 以下均又分为升序/降序
     * saleCount
     * skuPrice
     * hotScore
     */
    private String sort; // 排序条件

    /**
     * 过滤条件
     *
     * hasStock 是否有货
     * skuPrice 区间 1_500 _500 500_
     * brandId
     * catalog3Id
     * attrs
     */
    private Integer hasStock;

    private String skuPrice;

    private List<Long> brandId;

    private List<String> attrs;

    private Integer pageNum = 1;

}
