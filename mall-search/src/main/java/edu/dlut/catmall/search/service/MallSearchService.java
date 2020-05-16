package edu.dlut.catmall.search.service;

import edu.dlut.catmall.search.vo.SearchParam;
import edu.dlut.catmall.search.vo.SearchResult;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/16  09:43
 * DESCRIPTION:
 **/
public interface MallSearchService {

    /**
     * 检索服务
     * @param searchParam 所有检索参数
     * @return 检索结果
     */
    SearchResult search(SearchParam searchParam);

}
