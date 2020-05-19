package edu.dlut.catmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/4/29  23:58
 * DESCRIPTION:
 **/
@EnableFeignClients(basePackages = "edu.dlut.catmall.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("edu.dlut.catmall.product.dao")
@EnableRedisHttpSession
public class MallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }
}
