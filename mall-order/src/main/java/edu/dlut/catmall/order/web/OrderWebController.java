package edu.dlut.catmall.order.web;

import edu.dlut.catmall.order.service.OrderService;
import edu.dlut.catmall.order.vo.OrderConfirmVO;
import edu.dlut.catmall.order.vo.OrderSubmitVO;
import edu.dlut.catmall.order.vo.SubmitOrderResponseVO;
import edu.dlut.common.exception.NoStockException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String submitOrder(OrderSubmitVO submitVO, Model model, RedirectAttributes redirectAttributes) {
        // 下单 去创建订单 验证令牌 核算价格 锁定库存
        try {
            SubmitOrderResponseVO responseVO = orderService.submitOrder(submitVO);
            if (responseVO.getCode() == 0) {
                // 下单成功到选择支付方式页面
                model.addAttribute("submitOrderResponse", responseVO);
                return "pay";
            } else {
                // 订单失败返回到订单确认页面
                String msg = "下订单失败: ";
                switch (responseVO.getCode()) {
                    case 1 : msg += "订单信息过期, 请刷新后再次提交."; break;
                    case 2 : msg += "订单中的商品价格发生变化, 请刷新后再次提交."; break;
                    case 3 : msg += "库存锁定失败, 商品库存不足."; break;
                }
                redirectAttributes.addFlashAttribute("msg", msg);
                return "redirect:http://order.catmall.com/toTrade";
            }
        } catch (Exception e) {
            if (e instanceof NoStockException) {
                String message = e.getMessage();
                redirectAttributes.addFlashAttribute("msg", message);
            }
            return "redirect:http://order.catmall.com/toTrade";
        }
    }
}
