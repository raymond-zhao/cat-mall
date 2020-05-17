package edu.dlut.catmall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  10:07
 * DESCRIPTION:
 **/
@Data
@ToString
public class SkuItemSaleAttrVO {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVO> attrValues;

}
