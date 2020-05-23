package edu.dlut.catmall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  17:07
 * DESCRIPTION:
 **/
@Data
@Component
@ConfigurationProperties(prefix = "mall.threads")
public class ThreadPoolConfigProperties {

    private Integer corePoolSize;

    private Integer maxPoolSize;

    private Integer keepAliveTime;

}
