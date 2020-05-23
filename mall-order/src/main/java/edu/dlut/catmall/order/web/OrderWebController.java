package edu.dlut.catmall.order.web;

import edu.dlut.catmall.order.service.OrderService;
import edu.dlut.catmall.order.vo.OrderConfirmVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
