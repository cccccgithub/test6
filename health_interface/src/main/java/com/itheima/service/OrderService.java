package com.itheima.service;

import com.itheima.exception.MyException;
import com.itheima.pojo.Order;

import java.util.Map;

public interface OrderService {
    Order order(Map<String, String> map) throws MyException;

    Map<String,Object> findById(int id);
}
