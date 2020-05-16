package edu.dlut.catmall.search.controller;

import edu.dlut.catmall.search.service.MallSearchService;
import edu.dlut.catmall.search.vo.SearchParam;
import edu.dlut.catmall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/16  09:00
 * DESCRIPTION:
 **/
@Controller
public class SearchController {

    @Resource
    private MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model) {
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result", result);
        return "list";
    }

}
