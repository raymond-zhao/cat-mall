package edu.dlut.catmall.product.dao;

import edu.dlut.catmall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-29 23:53:34
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
