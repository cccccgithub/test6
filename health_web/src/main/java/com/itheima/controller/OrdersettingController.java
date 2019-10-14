package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import com.itheima.util.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    private OrdersettingService ordersettingService;
    
    @PostMapping("/upload")
    public Result upload (@RequestParam("excelFile")MultipartFile excelFile){
        SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            ArrayList<OrderSetting> oslist = new ArrayList();
            // Map<String,Object> map = null;
            OrderSetting orderSetting = null;
            if (strings.size()>0) {
                for (String[] string : strings) {
                    orderSetting=new OrderSetting();
                    //parse是将String转为date
                    orderSetting.setNumber(Integer.parseInt(string[1]));
                    orderSetting.setOrderDate(sdf.parse(string[0]));
                   // map.put("date",sdf.parse(string[0]));
                   // map.put("number",Integer.parseInt(string[1]));

                    oslist.add(orderSetting);
                }
                ordersettingService.upload(oslist);
                return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(false,MessageConstant.ORDERSETTING_FAIL);
    }

    @PostMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth (String month){
        String start=month+"-"+"01";
        String end=month+"-"+"31";
        List<OrderSetting> list= ordersettingService.getOrderSettingByMonth(start,end);
        List<Map<String, Integer>> maps = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d");
         Map<String,Integer> map = null;
        for (OrderSetting orderSetting : list) {
            String s = sdf.format(orderSetting.getOrderDate());
            map=new HashMap<>();
            map.put("date", Integer.valueOf(s));
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            maps.add(map);
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,maps);
    }
    
    
    @PostMapping("/updatenumber2")
    public Result updatenumber2 (@RequestBody OrderSetting orderSetting){
        ordersettingService.updatenumber2(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        
    }
}
