package edu.dlut.catmall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import edu.dlut.catmall.order.feign.CartFeign;
import edu.dlut.catmall.order.feign.MemberFeign;
import edu.dlut.catmall.order.feign.WareFeign;
import edu.dlut.catmall.order.interceptor.LoginUserInterceptor;
import edu.dlut.catmall.order.vo.MemberAddressVO;
import edu.dlut.catmall.order.vo.OrderConfirmVO;
import edu.dlut.catmall.order.vo.OrderItemVO;
import edu.dlut.common.to.SkuHasStockVO;
import edu.dlut.common.utils.R;
import edu.dlut.common.vo.MemberResponseVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.Query;

import edu.dlut.catmall.order.dao.OrderDao;
import edu.dlut.catmall.order.entity.OrderEntity;
import edu.dlut.catmall.order.service.OrderService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Resource
    private MemberFeign memberFeign;

    @Resource
    private CartFeign cartFeign;

    @Resource
    private WareFeign wareFeign;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVO confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        MemberResponseVO memberResponseVO = LoginUserInterceptor.loginUser.get();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> getAddressedTask = CompletableFuture.runAsync(() -> {
            // 1 远程查询会员的所有列表
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVO> addresses = memberFeign.getAddresses(memberResponseVO.getId());
            orderConfirmVO.setAddresses(addresses);
        }, threadPoolExecutor);

        CompletableFuture<Void> getCartItemsTask = CompletableFuture.runAsync(() -> {
            // 2 远程查询购物车所有选中的购物项
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVO> currentUserCartItems = cartFeign.getCurrentUserCartItems();
            orderConfirmVO.setItems(currentUserCartItems);
        }, threadPoolExecutor).thenRunAsync(() -> {
            List<OrderItemVO> items = orderConfirmVO.getItems();
            List<Long> collect = items.stream().map(o -> o.getSkuId()).collect(Collectors.toList());
            R hasStock = wareFeign.getSkusHasStock(collect);
            List<SkuHasStockVO> data = hasStock.getData(new TypeReference<List<SkuHasStockVO>>() {});
            if (!CollectionUtils.isEmpty(data)) {
                Map<Long, Boolean> map = data.stream().collect(Collectors.toMap(SkuHasStockVO::getSkuId, SkuHasStockVO::getHasStock));
                orderConfirmVO.setStocks(map);
            }
        }, threadPoolExecutor);

        // 3 查询用户积分
        orderConfirmVO.setIntegration(memberResponseVO.getIntegration());

        // 其他数据自动计算

        // TODO 防重令牌

        CompletableFuture.allOf(getAddressedTask, getCartItemsTask).get();
        return orderConfirmVO;
    }

}