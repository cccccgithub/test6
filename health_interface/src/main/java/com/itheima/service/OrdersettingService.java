package com.itheima.service;

import com.itheima.exception.MyException;
import com.itheima.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;

public interface OrdersettingService {

    void upload(ArrayList<OrderSetting> oslist);

    List<OrderSetting> getOrderSettingByMonth(String start, String end);

    void updatenumber2(OrderSetting orderSetting) throws MyException;

    List<OrderSetting> getOrderSetting();
}
