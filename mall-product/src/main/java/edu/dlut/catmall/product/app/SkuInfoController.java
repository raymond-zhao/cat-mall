package edu.dlut.catmall.product.app;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import edu.dlut.catmall.product.entity.SkuInfoEntity;
import edu.dlut.catmall.product.service.SkuInfoService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.R;



/**
 * sku信息
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:30:12
 */
@RestController
@RequestMapping("product/skuinfo")
public class SkuInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}/price")
    public R getPrice(@PathVariable("skuId") Long skuId) {
        return R.ok().setData(skuInfoService.getById(skuId).getPrice().toString());
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:skuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    // @RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
