package edu.dlut.common.constant;

import lombok.Getter;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/5  09:23
 * DESCRIPTION:
 **/
public class ProductConstant {

    @Getter
    public enum AttrEnum {

        ATTR_TYPE_SALE(0, "销售属性"),

        ATTR_TYPE_BASE(1, "基本属性");

        private int code;

        private String message;

        AttrEnum(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}
