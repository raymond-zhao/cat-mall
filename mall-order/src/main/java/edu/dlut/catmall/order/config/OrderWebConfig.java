package edu.dlut.catmall.order.config;

import edu.dlut.catmall.order.interceptor.LoginUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/23  14:52
 * DESCRIPTION:
 **/
@Configuration
public class OrderWebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginUserInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}
