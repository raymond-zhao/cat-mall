package edu.dlut.catmall.product.dao;

import edu.dlut.catmall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-29 23:53:35
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
