package edu.dlut.catmall.seckill.feign;

import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  00:17
 * DESCRIPTION:
 **/
@FeignClient("mall-coupon")
public interface CouponFeign {

    @GetMapping("/coupon/seckillsession/latest3DaySession")
    R getLatest3DaySession();

}
