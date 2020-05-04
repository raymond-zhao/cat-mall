package edu.dlut.catmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.dlut.catmall.product.dao.CategoryDao;
import edu.dlut.catmall.product.entity.CategoryEntity;
import edu.dlut.catmall.product.service.CategoryBrandRelationService;
import edu.dlut.catmall.product.service.CategoryService;
import edu.dlut.common.utils.PageUtils;
import edu.dlut.common.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 这个类继承了 ServiceImpl
        // 1. 查出所有分类列表
        List<CategoryEntity> entities = baseMapper.selectList(null); // 传入 null 代表查询所有

        // 2. 组装成树形结构
        List<CategoryEntity> levelMenu = entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu -> {
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                }).sorted((m1, m2) -> m1.getSort() == null ? 0 : m1.getSort() - (m2.getSort() == null ? 0 : m2.getSort())).collect(Collectors.toList());
        return levelMenu;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 检查需要删除的菜单是否被其他地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, path);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[path.size()]);
    }

    /**
     * 级联更新所有关联数据
     * @param category
     */
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    private List<Long> findParentPath(Long catelogId, List<Long> path) {
        // 收集当前结点
        path.add(catelogId);
        CategoryEntity categoryEntity = this.getById(catelogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), path);
        }
        return path;
    }

    /**
     * 递归查找所有菜单的子菜单
     *
     * @param root
     * @param all
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId())
                .map(categoryEntity -> {
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted((m1, m2) -> m1.getSort() == null ? 0 : m1.getSort() - (m2.getSort() == null ? 0 : m2.getSort()))
                .collect(Collectors.toList());
        return children;
    }

}