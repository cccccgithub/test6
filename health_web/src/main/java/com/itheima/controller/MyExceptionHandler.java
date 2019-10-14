package com.itheima.controller;

import com.itheima.entity.Result;
import com.itheima.exception.MyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(MyException.class)
    public Result handleMyException(MyException e){
        e.printStackTrace();
        return new Result(false,e.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return new Result(false,"发生了其他未知异常");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e){
        e.printStackTrace();
        return new Result(false,"权限不足");
    }
}
