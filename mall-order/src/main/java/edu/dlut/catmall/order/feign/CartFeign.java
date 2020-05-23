package edu.dlut.catmall.order.feign;

import edu.dlut.catmall.order.vo.OrderItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/23  15:39
 * DESCRIPTION:
 **/
@FeignClient("mall-cart")
public interface CartFeign {

    @GetMapping("/currentUserCartItems")
    List<OrderItemVO> getCurrentUserCartItems();

}
