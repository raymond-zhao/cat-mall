package edu.dlut.catmall.product.feign;

import edu.dlut.catmall.product.feign.fallback.SeckillFeignFallback;
import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  19:38
 * DESCRIPTION:
 **/
@FeignClient(value = "mall-seckill", fallback = SeckillFeignFallback.class)
public interface SeckillFeign {
    @GetMapping("/sku/seckill/{skuId}")
    R getSkuSeckillInfo(@PathVariable("skuId") Long skuId);
}
