package edu.dlut.catmall.cart.config;

import edu.dlut.catmall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  12:26
 * DESCRIPTION:
 **/
@Configuration
public class MallWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }

}
