package edu.dlut.catmall.search.service;

import edu.dlut.common.to.es.ESSkuModel;

import java.io.IOException;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/13  22:43
 * DESCRIPTION:
 **/
public interface ProductSaveService {
    boolean productStatusUp(List<ESSkuModel> esSkuModels) throws IOException;
}
