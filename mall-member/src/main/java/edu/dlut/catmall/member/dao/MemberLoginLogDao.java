package edu.dlut.catmall.member.dao;

import edu.dlut.catmall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:47:49
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
