package edu.dlut.catmall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/24  15:01
 * DESCRIPTION:
 **/
@Data
public class FareVO {

    private MemberAddressVO memberAddressVO;

    private BigDecimal fare;

}
