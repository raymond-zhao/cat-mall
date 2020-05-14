package edu.dlut.catmall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlut.catmall.product.entity.BrandEntity;
import edu.dlut.catmall.product.vo.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import edu.dlut.catmall.product.entity.CategoryBrandRelationEntity;
import edu.dlut.catmall.product.service.CategoryBrandRelationService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:30:12
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @GetMapping("/catelog/list")
    // @RequiresPermissions("product:categorybrandrelation:list")
    public R catelogList(@RequestParam Long brandId){
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService
                .list(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));

        return R.ok().put("data", data);
    }

    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam("catId") Long catId) {
        List<BrandEntity> vos = categoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVO> collect = vos.stream().map(item -> {
           BrandVO brandVO = new BrandVO();
           brandVO.setBrandId(item.getBrandId());
           brandVO.setBrandName(item.getName());
           return brandVO;
        }).collect(Collectors.toList());
        return R.ok().put("data", collect);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.saveDetail(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
