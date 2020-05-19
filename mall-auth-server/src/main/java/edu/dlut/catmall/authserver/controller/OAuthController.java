package edu.dlut.catmall.authserver.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import edu.dlut.catmall.authserver.feign.MemberFeign;
import edu.dlut.catmall.authserver.vo.SocialUser;
import edu.dlut.common.constant.AuthServerConstant;
import edu.dlut.common.utils.HttpUtils;
import edu.dlut.common.utils.R;
import edu.dlut.common.vo.MemberResponseVO;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/18  13:53
 * DESCRIPTION:
 **/
@Controller
public class OAuthController {

    @Resource
    private MemberFeign memberFeign;

    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "637884935");
        map.put("client_secret", "4df1aec66632a16781876f2064522657");
        map.put("grant_type", "authorization_code");
        map.put("redirect_uri", "http://auth.catmall.com/oauth2.0/weibo/success");
        map.put("code", code);
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", new HashMap<>(), map, new HashMap<>());
        if (response.getStatusLine().getStatusCode() == 200) {
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            // 获取用户的登录平台，然后判断用户是否该注册到系统中
            R r = memberFeign.oauthLogin(socialUser);
            if (r.getCode() == 0) {
                // session 子域共享问题
                MemberResponseVO loginUser = r.getData(new TypeReference<MemberResponseVO>() {});
                session.setAttribute(AuthServerConstant.LOGIN_USER, loginUser);
                return "redirect:http://catmall.com";
            } else {
                return "redirect:http://auth.catmall.com/login.html ";
            }
        } else {
            return "redirect:http://auth.catmall.com/login.html ";
        }
    }

}
