package edu.dlut.catmall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.dlut.catmall.product.entity.AttrEntity;
import edu.dlut.catmall.product.service.AttrAttrgroupRelationService;
import edu.dlut.catmall.product.service.AttrService;
import edu.dlut.catmall.product.service.CategoryService;
import edu.dlut.catmall.product.vo.AttrGroupRelationVO;
import edu.dlut.catmall.product.vo.AttrGroupWithAttrsVO;
import org.springframework.web.bind.annotation.*;

import edu.dlut.catmall.product.entity.AttrGroupEntity;
import edu.dlut.catmall.product.service.AttrGroupService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author raymond
 * @email qq740567396@gmail.com
 * @date 2020-04-30 09:30:13
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {

    @Resource
    private AttrGroupService attrGroupService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private AttrService attrService;

    @Resource
    private AttrAttrgroupRelationService relationService;

    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVO> vos) {
        relationService.saveBatch(vos);
        return R.ok();
    }

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long cateLogId) {
        // 1 查出当前分类下所有的属性分组
        // 2 查出每个属性分组的所有属性
        List<AttrGroupWithAttrsVO> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(cateLogId);
        return R.ok().put("data", vos);
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }

    @PostMapping("/attr/relation/delete")
    // @RequiresPermissions("product:attr:delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVO[] vos) {
        attrService.deleteRelation(vos);

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    // @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    // @RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
