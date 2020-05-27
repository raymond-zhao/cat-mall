package edu.dlut.common.to.mq;

import lombok.Data;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/26  23:44
 * DESCRIPTION:
 **/
@Data
public class StockLockedTO {

    private Long id;

    private StockDetailTO detail;

}
