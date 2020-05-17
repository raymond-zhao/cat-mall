package edu.dlut.catmall.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/17  19:25
 * DESCRIPTION:
 **/
@Data
public class MemberRegisterVO {

    private String username;

    private String password;

    private String phone;

}
