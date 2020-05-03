package edu.dlut.catmall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.dlut.catmall.product.entity.BrandEntity;
import edu.dlut.catmall.product.service.BrandService;
import edu.dlut.catmall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Slf4j
class MallProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void findPath() {
        Long[] path = categoryService.findCatelogPath(225L);
        log.info("path: {}", Arrays.asList(path));
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach(System.out::print);
    }

}
