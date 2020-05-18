package edu.dlut.catmall.authserver.vo;

import lombok.Data;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/18  09:51
 * DESCRIPTION:
 **/
@Data
public class UserLoginVO {

    private String account;

    private String password;
}
