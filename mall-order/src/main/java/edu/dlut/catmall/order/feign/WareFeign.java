package edu.dlut.catmall.order.feign;

import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/23  19:10
 * DESCRIPTION:
 **/
@FeignClient("mall-ware")
public interface WareFeign {

    @PostMapping("/ware/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);

}
