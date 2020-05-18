package edu.dlut.catmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.dlut.catmall.member.vo.MemberLoginVO;
import edu.dlut.catmall.member.vo.MemberRegisterVO;
import edu.dlut.catmall.member.vo.SocialUser;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.catmall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:47:50
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVO memberRegisterVO);

    MemberEntity login(MemberLoginVO memberLoginVO);

    MemberEntity login(SocialUser socialUser) throws Exception;
}

