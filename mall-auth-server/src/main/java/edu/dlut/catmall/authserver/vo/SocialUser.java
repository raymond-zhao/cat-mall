package edu.dlut.catmall.authserver.vo;

import lombok.Data;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/18  14:50
 * DESCRIPTION:
 **/
@Data
public class SocialUser {

    private String access_token;

    private String remind_in;

    private long expires_in;

    private String uid;

    private String isRealName;
}
