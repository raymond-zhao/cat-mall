package edu.dlut.catmall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.dlut.catmall.member.dao.MemberLevelDao;
import edu.dlut.catmall.member.entity.MemberLevelEntity;
import edu.dlut.catmall.member.exception.PhoneExistException;
import edu.dlut.catmall.member.exception.UsernameExistException;
import edu.dlut.catmall.member.vo.MemberLoginVO;
import edu.dlut.catmall.member.vo.MemberRegisterVO;
import edu.dlut.catmall.member.vo.SocialUser;
import edu.dlut.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

        memberEntity.setMobile(memberRegisterVO.getPhone());
        memberEntity.setUsername(memberRegisterVO.getUsername());
        memberEntity.setNickname(memberRegisterVO.getUsername());

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

    /**
     * 包含登录和注册功能
     * @param socialUser
     * @return
     */
    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {
        // 1 根据 uid 判断当前用户是否以前用社交平台登录过系统
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", socialUser.getUid()));
        if (!ObjectUtils.isEmpty(memberEntity)) {
            // 说明这个用户之前已经注册过
            MemberEntity update = new MemberEntity();
            update.setId(memberEntity.getId());
            update.setAccessToken(socialUser.getAccess_token());
            update.setExpiresIn(socialUser.getExpires_in());
            this.baseMapper.updateById(update);

            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());
            return memberEntity;
        } else {
            // 未找到则注册 根据社交平台的开放接口查询用户的开放信息存储到系统
            MemberEntity register = new MemberEntity();
            try {
                Map<String, String> query = new HashMap<>();
                query.put("access_token", socialUser.getAccess_token());
                query.put("uid", socialUser.getUid());
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<>(), query);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(json);
                    String name = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");
                    // ......
                    register.setNickname(name);
                    register.setGender("m".equals(gender) ? 1 : 0);
                    // .....
                }
            } catch (Exception e) {}
            register.setSocialUid(socialUser.getUid());
            register.setAccessToken(socialUser.getAccess_token());
            register.setExpiresIn(socialUser.getExpires_in());
            this.baseMapper.insert(register);
            return register;
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