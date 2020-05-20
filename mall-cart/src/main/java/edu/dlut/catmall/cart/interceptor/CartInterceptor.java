package edu.dlut.catmall.cart.interceptor;

import edu.dlut.catmall.cart.to.UserInfoTO;
import edu.dlut.common.constant.AuthServerConstant;
import edu.dlut.common.constant.CartConstant;
import edu.dlut.common.vo.MemberResponseVO;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  12:08
 * DESCRIPTION:
 **/
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTO> threadLocal = new ThreadLocal<>();

    /**
     * 目标方法拦截之前进行处理
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTO userInfoTO = new UserInfoTO();

        HttpSession session = request.getSession();
        MemberResponseVO memberResponseVO = (MemberResponseVO) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (!ObjectUtils.isEmpty(memberResponseVO))
            userInfoTO.setUserId(memberResponseVO.getId());
        Cookie[] cookies = request.getCookies();
        if (cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (CartConstant.TEMP_USER_COOKIE_NAME.equals(cookie.getName())) {
                    userInfoTO.setUserKey(cookie.getValue());
                    userInfoTO.setTempUser(true);
                }
            }
        }
        // 如果没有临时用户，一定创建一个临时用户。
        if (StringUtils.isEmpty(userInfoTO.getUserKey()))
            userInfoTO.setUserKey(UUID.randomUUID().toString());
        threadLocal.set(userInfoTO);
        return true;
    }

    /**
     * 后置请求 分配临时用户 让浏览器保存
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTO userInfoTO = threadLocal.get();
        // 判断用户是否已有 user-key 没有时才需要创建
        if (!userInfoTO.isTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTO.getUserKey());
            cookie.setDomain("catmall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
