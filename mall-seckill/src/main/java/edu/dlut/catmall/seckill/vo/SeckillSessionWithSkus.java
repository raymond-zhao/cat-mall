package edu.dlut.catmall.seckill.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/29  10:09
 * DESCRIPTION:
 **/
@Data
public class SeckillSessionWithSkus {

    private Long id;

    private String name;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private Date createTime;

    private List<SeckillSkuVO> relationSkus;

}
