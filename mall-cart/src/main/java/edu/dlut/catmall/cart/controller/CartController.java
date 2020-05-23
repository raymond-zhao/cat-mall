package edu.dlut.catmall.cart.controller;

import edu.dlut.catmall.cart.service.CartService;
import edu.dlut.catmall.cart.vo.Cart;
import edu.dlut.catmall.cart.vo.CartItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  11:01
 * DESCRIPTION:
 **/
@Controller
public class CartController {

    @Resource
    private CartService cartService;

    @GetMapping("/currentUserCartItems")
    @ResponseBody
    public List<CartItem> getCurrentUserCartItems() {
        return cartService.getUserCartItems();
    }

    @GetMapping("/cart.html")
    public String cartList(Model model) throws ExecutionException, InterruptedException {
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) {
        cartService.countItem(skuId, num);
        return "redirect:http://cart.catmall.com/cart.html";
    }

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId) {
        cartService.deleteItem(skuId);
        return "redirect:http://cart.catmall.com/cart.html";
    }

    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId, @RequestParam("checked") Integer checked) {
        cartService.checkItem(skuId, checked);
        return "redirect:http://cart.catmall.com/cart.html";
    }

    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes attributes) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId, num);
        attributes.addAttribute("skuId", skuId);
        return "redirect:http://cart.catmall.com/addToCartSuccess.html";
    }

    /**
     * 解决页面刷新重复提交的问题
     *
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model) {
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);
        return "success";
    }

}
