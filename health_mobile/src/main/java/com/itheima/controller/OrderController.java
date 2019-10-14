package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;

import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @Reference
    private OrdersettingService ordersettingService;

    @PostMapping("/submit")
    public Result submit (@RequestBody Map<String,String> map){

        Jedis jedis = jedisPool.getResource();
        String key =map.get("telephone")+ RedisMessageConstant.SENDTYPE_ORDER;
        String value = jedis.get(key);
        if (StringUtils.isEmpty(value)) {
            return new Result(false,"请重新获取验证码");
        }


        System.out.println(value.equals(map.get("validateCode")));

        //value!=map.get("validateCode")

        if (!value.equals(map.get("validateCode"))) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        jedis.del(key);
        map.put("orderType",Order.ORDERTYPE_WEIXIN);
        map.put("orderStatus",Order.ORDERSTATUS_NO);
        Order order= orderService.order(map);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @PostMapping("/findById")
    public Result findById (int id){

       Map<String,Object> map=orderService.findById(id);
       return new Result(true,MessageConstant.ORDER_SUCCESS,map);
    }

}
