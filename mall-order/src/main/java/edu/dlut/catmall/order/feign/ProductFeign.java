package edu.dlut.catmall.order.feign;

import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  18:02
 * DESCRIPTION:
 **/
@FeignClient("mall-product")
public interface ProductFeign {
    @GetMapping("/product/spuinfo/skuId/{id}")
    R getSpuInfoBySkuId(@PathVariable("id") Long skuId);
}
