package edu.dlut.catmall.member.exception;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  19:45
 * DESCRIPTION:
 **/
public class UsernameExistException extends RuntimeException {
    public UsernameExistException() {
        super("用户名已存在");
    }
}
