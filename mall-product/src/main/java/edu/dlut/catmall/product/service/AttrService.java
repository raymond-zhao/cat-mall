package edu.dlut.catmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.product.vo.AttrGroupRelationVO;
import edu.dlut.catmall.product.vo.AttrResponseVO;
import edu.dlut.catmall.product.vo.AttrVO;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-29 23:53:36
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVO attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrResponseVO getAttrInfo(Long attrId);

    void updateAttr(AttrVO attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVO[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    List<Long> selectSearchAttrs(List<Long> attrIds);
}

