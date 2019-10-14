package com.itheima.dao;

import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface OrdersettingDao {

    @Select("select * from t_ordersetting where orderdate = #{date}")
    OrderSetting findOrdersettingBydate(Object date);

    @Update("update t_ordersetting set number = #{number} where orderdate = #{orderDate}")
    void updateNumByDate(OrderSetting orderSetting);

    @Insert("insert into t_ordersetting values(null,#{date},#{number},0) ")
    void addOrdersetting(Map<String, Object> map);

    @Insert("insert into t_ordersetting values(null,#{orderDate},#{number},#{reservations}) ")
    void add(OrderSetting orderSetting);

    @Select("select * from t_ordersetting where orderdate between #{start} and #{end}")
    List<OrderSetting> getOrderSettingByMonth(Map<String, Object> params);

    @Update("update t_ordersetting set reservations = reservations+1 where orderDate = #{orderDate}")
    void updateReservations(Order order);

    @Select("select * from t_ordersetting")
    List<OrderSetting> getOrderSetting();
}
