package edu.dlut.catmall.product.vo;

import edu.dlut.catmall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/5  17:20
 * DESCRIPTION:
 **/
@Data
public class AttrGroupWithAttrsVO {

    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
