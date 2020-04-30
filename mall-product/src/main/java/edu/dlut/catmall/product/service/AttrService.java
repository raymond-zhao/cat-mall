package edu.dlut.catmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.product.entity.AttrEntity;

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
}

