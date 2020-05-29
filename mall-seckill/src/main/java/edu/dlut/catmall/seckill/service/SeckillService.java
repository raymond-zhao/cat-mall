package edu.dlut.catmall.seckill.service;

import edu.dlut.catmall.seckill.to.SeckillSkuRedisTO;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  00:15
 * DESCRIPTION:
 **/
public interface SeckillService {

    void uploadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTO> getCurrentSeckillSkus();

    SeckillSkuRedisTO getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
