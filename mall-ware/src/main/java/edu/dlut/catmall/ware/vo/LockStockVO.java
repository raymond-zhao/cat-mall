package edu.dlut.catmall.ware.vo;

import lombok.Data;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  20:05
 * DESCRIPTION:
 **/
@Data
public class LockStockVO {

    private Long skuId;

    private Integer num;

    private Boolean  locked;
    
}
