package edu.dlut.catmall.cart.to;

import lombok.Data;
import lombok.ToString;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/20  12:11
 * DESCRIPTION:
 **/
@Data
@ToString
public class UserInfoTO {

    private Long userId;

    private String userKey;

    private boolean tempUser = false;

}
