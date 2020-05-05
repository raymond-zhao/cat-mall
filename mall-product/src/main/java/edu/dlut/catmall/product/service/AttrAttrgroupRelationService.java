package edu.dlut.catmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.product.vo.AttrGroupRelationVO;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-29 23:53:36
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveBatch(List<AttrGroupRelationVO> vos);
}

