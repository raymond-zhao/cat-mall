package edu.dlut.catmall.search.controller;

import edu.dlut.catmall.search.service.ProductSaveService;
import edu.dlut.common.exception.BizCodeEnum;
import edu.dlut.common.to.es.ESSkuModel;
import edu.dlut.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/13  22:41
 * DESCRIPTION:
 **/
@Slf4j
@RequestMapping("/search/save")
@RestController
public class ESSaveController {

    @Resource
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<ESSkuModel> esSkuModels) {

        boolean result;

        try {
            result = productSaveService.productStatusUp(esSkuModels);
        } catch (IOException e) {
            log.error("ESSaveController 商品上架错误: {}", e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if (!result) {
            return R.ok();
        } else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(), BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
