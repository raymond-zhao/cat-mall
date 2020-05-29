package edu.dlut.catmall.product.vo;

import edu.dlut.catmall.product.entity.SkuImagesEntity;
import edu.dlut.catmall.product.entity.SkuInfoEntity;
import edu.dlut.catmall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  10:02
 * DESCRIPTION:
 **/
@Data
public class SkuItemVO {

    private SkuInfoEntity info;

    private boolean hasStock = true;

    private List<SkuImagesEntity> images;

    private List<SkuItemSaleAttrVO> saleAttr;

    private SpuInfoDescEntity desp;

    private List<SpuItemAttrGroupVO> groupAttrs;

    private SeckillInfoVO seckillInfo;

}
