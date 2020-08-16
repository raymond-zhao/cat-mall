package edu.dlut.catmall.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  16:53
 * DESCRIPTION: 线程池配置类
 **/
@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties poolConfigProperties) {
        return new ThreadPoolExecutor(poolConfigProperties.getCorePoolSize(),
                poolConfigProperties.getMaxPoolSize(), poolConfigProperties.getKeepAliveTime(),
                TimeUnit.SECONDS, new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }
}
