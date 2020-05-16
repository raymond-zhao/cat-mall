package edu.dlut.catmall.search.vo;

import edu.dlut.common.to.es.ESSkuModel;
import lombok.Data;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/16  09:59
 * DESCRIPTION:
 **/
@Data
public class SearchResult {

    /**
     * 查询到的所有商品信息
     */
    private List<ESSkuModel> products;

    /**
     * 分页信息
     */
    private Integer pageNum;

    private Long totalRecords;

    private Integer totalPages;

    private List<Integer> pageNavs;

    /**
     * 品牌信息
     * 当前查询到的结果 所有涉及到的品牌
     */
    private List<BrandVO> brands;

    @Data
    public static class BrandVO {

        private Long brandId;

        private String brandName;

        private String brandImg;

    }

    /**
     * 属性信息
     * 当前查询到的结果 所有涉及到的属性
     */
    private List<AttrVO> attrs;

    @Data
    public static class AttrVO {

        private Long attrId;

        private String attrName;

        private List<String> attrValue;

    }

    /**
     * 分类信息
     * 当前查询到的结果 所有涉及到的分类
     */
    private List<CatalogVO> catalogs;

    @Data
    public static class CatalogVO {

        private Long catalogId;

        private String catalogName;

    }
}
