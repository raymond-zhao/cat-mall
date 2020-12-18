package edu.dlut.catmall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/15  17:58
 * DESCRIPTION:
 **/
@Configuration
public class MyRedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() throws IOException {
        // 默认连接地址 127.0.0.1:6379
        // RedissonClient redisson = Redisson.create();

        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }
}
