package edu.dlut.catmall.order.web;

import edu.dlut.catmall.order.service.OrderService;
import edu.dlut.catmall.order.vo.OrderConfirmVO;
import edu.dlut.catmall.order.vo.OrderSubmitVO;
import edu.dlut.catmall.order.vo.SubmitOrderResponseVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/23  12:19
 * DESCRIPTION:
 **/
@Controller
public class OrderWebController {

    @Resource
    private OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVO orderConfirmVO = orderService.confirmOrder();
        model.addAttribute("orderConfirmData", orderConfirmVO);
        return "confirm";
    }

    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVO submitVO) {
        // 下单 去创建订单 验证令牌 核算价格 锁定库存
        SubmitOrderResponseVO responseVO = orderService.submitOrder(submitVO);
        if (responseVO.getCode() == 0) {
            // 下单成功到选择支付方式页面
            return "pay";
        } else {
            // 字啊单失败返回到订单确认页面
            return "redirect:http://order.catmall.com/toTrade";
        }
    }
}
