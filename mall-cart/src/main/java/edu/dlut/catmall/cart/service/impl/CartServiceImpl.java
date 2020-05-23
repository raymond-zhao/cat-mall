package edu.dlut.catmall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import edu.dlut.catmall.cart.feign.ProductFeign;
import edu.dlut.catmall.cart.interceptor.CartInterceptor;
import edu.dlut.catmall.cart.service.CartService;
import edu.dlut.catmall.cart.to.UserInfoTO;
import edu.dlut.catmall.cart.vo.Cart;
import edu.dlut.catmall.cart.vo.CartItem;
import edu.dlut.catmall.cart.vo.SkuInfoVO;
import edu.dlut.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  11:59
 * DESCRIPTION:
 **/
@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ProductFeign productFeign;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    private final String CART_PREFIX = "mall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String res = (String) cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(res)) {
            CartItem cartItem = new CartItem();
            // 购物车无此商品
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                // 1 远程调用查询商品信息
                R info = productFeign.info(skuId);
                SkuInfoVO data = info.getData("skuInfo", new TypeReference<SkuInfoVO>() {
                });
                // 2 将新商品添加到购物车
                cartItem.setChecked(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());
            }, threadPoolExecutor);
            // 3 远程查询 sku 的属性信息
            CompletableFuture<Void> getSkuSaleAttrValuesTask = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeign.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttrs(skuSaleAttrValues);
            }, threadPoolExecutor);
            CompletableFuture.allOf(getSkuInfoTask, getSkuSaleAttrValuesTask).get();

            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        } else {
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String res = (String) cartOps.get(skuId.toString());
        return JSON.parseObject(res, CartItem.class);
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTO userInfoTO = CartInterceptor.threadLocal.get();
        Cart cart = new Cart();
        if (!ObjectUtils.isEmpty(userInfoTO.getUserId())) {
            String cartKey = CART_PREFIX + userInfoTO.getUserId();
            String tempCartKey = CART_PREFIX + userInfoTO.getUserKey();
            // 已登录
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (!CollectionUtils.isEmpty(tempCartItems)) {
                // 如果临时购物车中的数据还未进行合并
                for (CartItem cartItem : tempCartItems)
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                // 添加完后删除临时购物车
                clearCart(tempCartKey);
            }
            cart.setItems(getCartItems(cartKey));
        } else {
            // 未登录
            // 获取临时购物车中的所有购物项
            String cartKey = CART_PREFIX + userInfoTO.getUserKey();
            cart.setItems(getCartItems(cartKey));
        }
        return cart;
    }

    @Override
    public void clearCart(String cartKey) {
        stringRedisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setChecked(checked == 1);
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public void countItem(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTO userInfoTO = CartInterceptor.threadLocal.get();
        if (!ObjectUtils.isEmpty(userInfoTO)) {
            String cartKey = CART_PREFIX + userInfoTO.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            List<CartItem> collect = cartItems.stream().filter(o -> o.getChecked())
                    .map(o -> {
                        R price = productFeign.getPrice(o.getSkuId());
                        // 更新为最新价格
                        String data = (String) price.get("data");
                        o.setPrice(new BigDecimal(data));
                        return o;
                    }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    /**
     * 获取要操作的购物车
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTO userInfoTO = CartInterceptor.threadLocal.get();
        String cartKey = !ObjectUtils.isEmpty(userInfoTO.getUserId()) ? CART_PREFIX + userInfoTO.getUserId() : CART_PREFIX + userInfoTO.getUserKey();
        return stringRedisTemplate.boundHashOps(cartKey);
    }

    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (!CollectionUtils.isEmpty(values)) {
            return values.stream().map(o -> JSON.parseObject((String) o, CartItem.class)).collect(Collectors.toList());
        }
        return null;
    }
}
