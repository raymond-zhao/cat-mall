package edu.dlut.catmall.product.feign;

import edu.dlut.common.to.es.ESSkuModel;
import edu.dlut.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/13  23:06
 * DESCRIPTION:
 **/
@FeignClient("mall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<ESSkuModel> esSkuModels);
}
