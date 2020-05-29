package edu.dlut.catmall.seckill.controller;

import edu.dlut.catmall.seckill.service.SeckillService;
import edu.dlut.catmall.seckill.to.SeckillSkuRedisTO;
import edu.dlut.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  17:33
 * DESCRIPTION:
 **/
@RestController
public class SeckillController {

    @Resource
    private SeckillService seckillService;

    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTO> vos = seckillService.getCurrentSeckillSkus();
        return R.ok().setData(vos);
    }

    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTO to = seckillService.getSkuSeckillInfo(skuId);
        return R.ok().setData(to);
    }

    @GetMapping("/kill")
    public R seckill(@RequestParam("killId") String killId,
                     @RequestParam("key") String key,
                     @RequestParam("num") Integer num) {
        String orderSn = seckillService.kill(killId, key, num);
        return R.ok().setData(orderSn);
    }

}
