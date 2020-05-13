package edu.dlut.catmall.search.service.impl;

import com.alibaba.fastjson.JSON;
import edu.dlut.catmall.search.config.MallElasticSearchConfig;
import edu.dlut.catmall.search.constant.ESConstant;
import edu.dlut.catmall.search.service.ProductSaveService;
import edu.dlut.common.to.es.ESSkuModel;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/13  22:45
 * DESCRIPTION:
 **/
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<ESSkuModel> esSkuModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (ESSkuModel esSkuModel : esSkuModels) {
            IndexRequest indexRequest = new IndexRequest(ESConstant.PRODUCT_INDEX);
            indexRequest.id(esSkuModel.getSkuId().toString());
            String s = JSON.toJSONString(esSkuModel);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }

        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, MallElasticSearchConfig.COMMON_OPTIONS);

        // TODO 如果批量保存出现错误
        boolean result = bulkResponse.hasFailures();
        List<String> collect = Arrays.stream(bulkResponse.getItems()).map(BulkItemResponse::getId).collect(Collectors.toList());
        log.error("商品上架完成, {}", collect);
        return result;
    }
}
