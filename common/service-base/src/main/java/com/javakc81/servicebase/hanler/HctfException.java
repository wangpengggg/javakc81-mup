package com.javakc81.servicebase.hanler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HctfException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;
}
