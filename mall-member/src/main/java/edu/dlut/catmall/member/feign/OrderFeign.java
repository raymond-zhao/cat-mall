package edu.dlut.catmall.member.feign;

import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/28  11:09
 * DESCRIPTION:
 **/
@FeignClient("mall-order")
public interface OrderFeign {

    @RequestMapping("/order/order/listWithItems")
    R listWithItems(@RequestBody Map<String, Object> params);

}
