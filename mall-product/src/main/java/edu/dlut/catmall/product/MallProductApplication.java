package edu.dlut.catmall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/4/29  23:58
 * DESCRIPTION:
 **/
@SpringBootApplication
@MapperScan("edu.dlut.catmall.product.dao")
public class MallProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallProductApplication.class, args);
    }
}
