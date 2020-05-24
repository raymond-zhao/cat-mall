package edu.dlut.catmall.ware.feign;

import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  14:36
 * DESCRIPTION:
 **/
@FeignClient("mall-member")
public interface MemberFeign {

    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    R addrInfo(@PathVariable("id") Long id);

}
