package edu.dlut.catmall.member.dao;

import edu.dlut.catmall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:47:50
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
