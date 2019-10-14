package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrdersettingDao;
import com.itheima.exception.MyException;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrdersettingService.class)
public class OrdersettingServiceImpl implements OrdersettingService {

    @Autowired
    private OrdersettingDao ordersettingDao;

    @Transactional
    @Override
    public void upload(ArrayList<OrderSetting> oslist) {
        if (null!=oslist&&oslist.size()>0) {
            for (OrderSetting orderSettings : oslist) {

                System.out.println(orderSettings);
                OrderSetting orderSetting= ordersettingDao.findOrdersettingBydate(orderSettings.getOrderDate());
                if (null!=orderSetting) {
                    //不为空说明存在,则修改
                    orderSetting.setNumber(orderSettings.getNumber());
                    ordersettingDao.updateNumByDate(orderSetting);
                }else{
                    ordersettingDao.add(orderSettings);

                }
            }
        }
    }

    @Override
    public List<OrderSetting> getOrderSettingByMonth(String start, String end) {
         Map<String,Object> params = new HashMap<>();
         params.put("start",start);
         params.put("end",end);
        return ordersettingDao.getOrderSettingByMonth(params);
    }

    @Override
    public void updatenumber2(OrderSetting orderSetting) throws MyException{
        OrderSetting osByDate = ordersettingDao.findOrdersettingBydate(orderSetting.getOrderDate());
        if (null!=osByDate) {
             if(orderSetting.getNumber()>osByDate.getReservations()){
                 orderSetting.setReservations(osByDate.getReservations());
                 ordersettingDao.updateNumByDate(orderSetting);
             }else{
                 throw new MyException("可预约人数不能少于已经预约人数");
             }
        }else{
            Map<String,Object> map = new HashMap<>();
            map.put("orderDate",orderSetting.getOrderDate());
            map.put("number",orderSetting.getNumber());
            ordersettingDao.addOrdersetting(map);
        }


    }

    @Override
    public List<OrderSetting> getOrderSetting() {
        return ordersettingDao.getOrderSetting();
    }
}
