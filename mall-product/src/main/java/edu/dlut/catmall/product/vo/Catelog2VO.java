package edu.dlut.catmall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @AUTHOR: raymond
 * @DATETIME: 2020/5/14  20:58
 * DESCRIPTION:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catelog2VO {

    private String catalogId;

    private List<Catelog3VO> catalog3List;

    private String id;

    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Catelog3VO {

        private String catalog2Id;

        private String id;

        private String name;

    }

}
