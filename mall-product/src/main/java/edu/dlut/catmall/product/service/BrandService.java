package edu.dlut.catmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-29 23:53:35
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

