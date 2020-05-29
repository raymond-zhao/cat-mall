package edu.dlut.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  23:36
 * DESCRIPTION:
 **/
@Data
public class SeckillOrderTO {

    private String orderSn;

    private Long promotionSessionId; // 秒杀活动批次

    private Long skuId;

    private BigDecimal seckillPrice;

    private Integer num;

    private Long memberId;

}
