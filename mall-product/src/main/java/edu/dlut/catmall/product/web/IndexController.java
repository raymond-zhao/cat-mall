package edu.dlut.catmall.product.web;

import edu.dlut.catmall.product.entity.CategoryEntity;
import edu.dlut.catmall.product.service.CategoryService;
import edu.dlut.catmall.product.vo.Catelog2VO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/14  20:47
 * DESCRIPTION:
 **/
@Controller
public class IndexController {

    @Resource
    private CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        // TODO 1 查出所有的 1 级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categories();
        // 视图解析器进行拼串
        // classpath:/templates/ + result + .html
        model.addAttribute("categories", categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2VO>> getCatalogJson() throws InterruptedException {
        return categoryService.getCatalogJson();
    }
}
