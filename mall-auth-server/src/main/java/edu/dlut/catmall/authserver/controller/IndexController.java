package edu.dlut.catmall.authserver.controller;

import com.alibaba.fastjson.TypeReference;
import edu.dlut.catmall.authserver.feign.MemberFeign;
import edu.dlut.catmall.authserver.feign.ThirdPartyFeign;
import edu.dlut.catmall.authserver.vo.UserLoginVO;
import edu.dlut.catmall.authserver.vo.UserRegisterVO;
import edu.dlut.common.constant.AuthServerConstant;
import edu.dlut.common.exception.BizCodeEnum;
import edu.dlut.common.utils.R;
import edu.dlut.common.vo.MemberResponseVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  13:42
 * DESCRIPTION:
 **/
@Controller
public class IndexController {

    @Resource
    private ThirdPartyFeign thirdPartyFeign;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MemberFeign memberFeign;

    @GetMapping("/sms/send")
    @ResponseBody
    public R sendSMS(@RequestParam("phone") String phone) {

        String redisCode = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)) {
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000)
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(), BizCodeEnum.SMS_CODE_EXCEPTION.getMsg());
        }

        String code = UUID.randomUUID().toString().substring(0, 5);
        String redisStorage = code + "_" + System.currentTimeMillis();
        // 为验证码设置过期时间
        stringRedisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, redisStorage, 10, TimeUnit.MINUTES);

        thirdPartyFeign.sendSMSCode(phone, code);
        return R.ok();
    }

    @PostMapping("/reg")
    public String register(@Valid UserRegisterVO vo, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.catmall.com/register.html";
        }

        // 校验验证码
        String code = vo.getCode();
        String s = stringRedisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (!StringUtils.isEmpty(s)) {
            if (code.equals(s.split("_")[0])) {
                stringRedisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                R register = memberFeign.register(vo);
                if (register.getCode() == 0) {
                    return "redirect:http://auth.catmall.com/login.html";
                } else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg", register.getData("msg", new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors", errors);
                    return "redirect:http://auth.catmall.com/register.html";
                }
            } else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.catmall.com/register.html";
            }
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.catmall.com/register.html";
        }
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        return !ObjectUtils.isEmpty(attribute) ? "redirect:http://catmall.com" : "login";
    }

    @PostMapping("/login")
    public String login(UserLoginVO userLoginVO, RedirectAttributes redirectAttributes,
                        HttpSession session) {
        R login = memberFeign.login(userLoginVO);
        if (login.getCode() != 0) {
            MemberResponseVO loginUser = login.getData(new TypeReference<MemberResponseVO>() {});
            session.setAttribute(AuthServerConstant.LOGIN_USER, loginUser);
            return "redirect:http://catmall.com";
        } else {
            Map<String, String> errors = new HashMap<>();
            errors.put("msg", login.getData("msg", new TypeReference<String>(){}));
            redirectAttributes.addAttribute("errors", errors);
            return "redirect:http://auth.catmall.com/login.html";
        }
    }

}
