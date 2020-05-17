package edu.dlut.catmall.member.exception;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  19:44
 * DESCRIPTION:
 **/
public class PhoneExistException extends RuntimeException {
    public PhoneExistException() {
        super("手机号已存在");
    }
}
