package edu.dlut.catmall.product.feign.fallback;

import edu.dlut.catmall.product.feign.SeckillFeign;
import edu.dlut.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/30  16:59
 * DESCRIPTION:
 **/
@Component
@Slf4j
public class SeckillFeignFallback implements SeckillFeign {
    @Override
    public R getSkuSeckillInfo(Long skuId) {
        log.info("熔断方法调用...product.feign.SeckillFeign");
        return null;
    }
}
