package edu.dlut.catmall.product.feign;

import edu.dlut.common.to.SkuHasStockVO;
import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/13  13:29
 * DESCRIPTION:
 **/
@FeignClient("mall-ware")
public interface WareFeignService {

    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);

}
