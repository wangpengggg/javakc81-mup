package com.javakc81.servicebase.hanler;


import com.javakc81.commonutils.api.APICODE;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常数据格式返回
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public APICODE errorHandler(Exception e) {
        e.printStackTrace();
        return APICODE.ERROR().message("服务器异常：Exception");
    }

    @ExceptionHandler(HctfException.class)
    @ResponseBody
    public APICODE errorHandler(HctfException e) {
        e.printStackTrace();
        return APICODE.ERROR().code(e.getCode()).message(e.getMessage());
    }


}
