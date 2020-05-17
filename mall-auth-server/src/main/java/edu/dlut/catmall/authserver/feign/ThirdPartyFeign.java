package edu.dlut.catmall.authserver.feign;

import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  17:32
 * DESCRIPTION:
 **/
@FeignClient("mall-third-party")
public interface ThirdPartyFeign {

    @GetMapping("/sms/send")
    R sendSMSCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
