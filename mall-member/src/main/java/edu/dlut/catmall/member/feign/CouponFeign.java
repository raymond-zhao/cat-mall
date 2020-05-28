package edu.dlut.catmall.member.feign;

import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/4/30  12:36
 * DESCRIPTION:
 **/
@FeignClient("mall-coupon")
public interface CouponFeign {

    @GetMapping("/coupon/coupon/member/list")
    R memberList();
}
