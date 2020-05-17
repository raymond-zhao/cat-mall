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
public class SpuItemAttrGroupVO {

    private String groupName;

    private List<Attr> attrs;

}
