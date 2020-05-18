package edu.dlut.catmall.member.service.impl;

import edu.dlut.catmall.member.dao.MemberLevelDao;
import edu.dlut.catmall.member.entity.MemberLevelEntity;
import edu.dlut.catmall.member.exception.PhoneExistException;
import edu.dlut.catmall.member.exception.UsernameExistException;
import edu.dlut.catmall.member.vo.MemberLoginVO;
import edu.dlut.catmall.member.vo.MemberRegisterVO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.Query;

import edu.dlut.catmall.member.dao.MemberDao;
import edu.dlut.catmall.member.entity.MemberEntity;
import edu.dlut.catmall.member.service.MemberService;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Resource
    private MemberLevelDao memberLevelDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVO memberRegisterVO) {
        MemberEntity memberEntity = new MemberEntity();

        MemberLevelEntity memberLevelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(memberLevelEntity.getId());

        checkPhoneUnique(memberRegisterVO.getPhone());
        checkUsernameUnique(memberRegisterVO.getUsername());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(memberRegisterVO.getPassword());
        memberEntity.setPassword(encode);

        this.baseMapper.insert(memberEntity);
    }

    @Override
    public MemberEntity login(MemberLoginVO memberLoginVO) {
        String account = memberLoginVO.getAccount();
        String password = memberLoginVO.getPassword();

        // 去数据库查询
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", account).or().eq("mobile", account));
        if (!ObjectUtils.isEmpty(memberEntity)) {
            String passwordDB = memberEntity.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, passwordDB)) {
                return memberEntity;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void checkUsernameUnique(String username) throws UsernameExistException {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count > 0)
            throw new UsernameExistException();
    }

    private void checkPhoneUnique(String phone) throws PhoneExistException {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count > 0)
            throw new PhoneExistException();
    }

}