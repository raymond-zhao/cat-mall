package edu.dlut.catmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.product.vo.SpuSaveVO;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-29 23:53:35
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVO vo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    void up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}

