package com.javakc81.pms.dispord.vo;

import lombok.Data;

/**
 * 条件查询封装对象
 */
@Data
public class DispOrdQuery {

    private String orderName;

    private String beginDate;

    private String endDate;

}
