package edu.dlut.catmall.order.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import edu.dlut.catmall.order.entity.OrderEntity;
import edu.dlut.catmall.order.service.OrderService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.R;

import javax.annotation.Resource;


/**
 * 订单
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:50:58
 */
@RestController
@RequestMapping("order/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 获取订单状态
     *
     * @param orderSn
     * @return
     */
    @GetMapping("/status/{orderSn}")
    public R getOrderStatus(@PathVariable("orderSn") String orderSn) {
        OrderEntity orderEntity = orderService.getOrderByOrderSn(orderSn);
        return R.ok().setData(orderEntity);
    }

    /**
     * 查询当前登录用户的所有订单 分页查询订单
     *
     * @param params
     * @return
     */
    @RequestMapping("/listWithItems")
    public R listWithItems(@RequestBody Map<String, Object> params) {
        PageUtils page = orderService.queryPageWithItems(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("order:order:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id) {
        OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
