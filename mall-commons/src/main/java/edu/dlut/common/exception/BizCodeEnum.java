package edu.dlut.common.exception;

import lombok.Getter;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/2  09:34
 * DESCRIPTION:
 *  * 错误码和错误信息定义类
 *  * 1. 错误码定义规则为5为数字
 *  * 2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
 *  * 3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
 *  * 错误码列表：
 *  *  10: 通用
 *  *      001：参数格式校验
 *  *  11: 商品
 *  *  12: 订单
 *  *  13: 购物车
 *  *  14: 物流
 *  *  15: 用户
 *  *  22: 库存
 **/
@Getter
public enum BizCodeEnum {

    UNKNOWN_EXCEPTION(10000,"系统未知异常"),

    VALID_EXCEPTION(10001,"参数格式校验失败"),

    SMS_CODE_EXCEPTION(10002,"短信频率太快"),

    TOO_MANY_REQUESTS(10003,"请求频率过高"),

    PRODUCT_UP_EXCEPTION(11000, "商品上架异常"),

    USERNAME_EXIST_EXCEPTION(15001, "用户名已存在"),

    PHONE_EXIST_EXCEPTION(15002, "手机号已存在"),

    LOGIN_EXCEPTION(15003, "用户名或密码错误"),

    NO_STOCK_EXCEPTION(22000, "商品库存不足")
    ;

    private int code;

    private String msg;

    BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
