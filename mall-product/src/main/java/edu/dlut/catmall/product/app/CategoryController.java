package edu.dlut.catmall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dlut.catmall.product.entity.CategoryEntity;
import edu.dlut.catmall.product.service.CategoryService;
import edu.dlut.common.utils.R;


/**
 * 商品三级分类
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:30:12
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有分类及其子分类，并组装成树形结构。
     */
    @RequestMapping("/list/tree")
    // @RequiresPermissions("product:category:list")
    public R list(@RequestParam Map<String, Object> params) {
        List<CategoryEntity> categoryEntityList = categoryService.listWithTree();
        return R.ok().put("data", categoryEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    // @RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 批量修改排序编号
     */
    @RequestMapping("/update/sort")
    // @RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody CategoryEntity[] categories) {
        categoryService.updateBatchById(Arrays.asList(categories));

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
