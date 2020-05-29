package edu.dlut.catmall.seckill.to;

import edu.dlut.catmall.seckill.vo.SkuInfoVO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  16:41
 * DESCRIPTION:
 **/
@Data
public class SeckillSkuRedisTO {

    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;
    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private Integer seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    private Long startTime;

    private Long endTime;

    // 秒杀随机码
    private String randomCode;

    private SkuInfoVO skuInfo;

}
