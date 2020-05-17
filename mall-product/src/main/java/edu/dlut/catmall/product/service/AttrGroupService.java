package edu.dlut.catmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.product.vo.AttrGroupWithAttrsVO;
import edu.dlut.catmall.product.vo.SpuItemAttrGroupVO;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-29 23:53:35
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCatelogId(Long cateLogId);

    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuIdAndCatalogId(Long spuId, Long catalogId);
}

