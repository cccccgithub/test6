package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrdersettingDao;
import com.itheima.entity.Result;
import com.itheima.exception.MyException;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService{

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrdersettingDao ordersettingDao;

    @Transactional
    @Override
    public Order order(Map<String, String> map) throws MyException{
        String orderDate = map.get("orderDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = sdf.parse(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        OrderSetting os = ordersettingDao.findOrdersettingBydate(date);
            if (null==os) {
                throw  new MyException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            if (os.getReservations()>=os.getNumber()) {
                throw  new MyException(MessageConstant.ORDER_FULL);
            }

            Member member1 = memberDao.findByTelephone(map.get("telephone"));

            if (null==member1) {
                member1=new Member();
                member1.setIdCard(map.get("idCard"));
                member1.setPhoneNumber(map.get("telephone"));
                member1.setName(map.get("name"));
                member1.setRegTime(new Date());
                member1.setSex(map.get("sex"));
                memberDao.addMember(member1);
            }
            Integer member1Id = member1.getId();
            Order order = new Order();
            order.setMemberId(member1Id);
            order.setPackageId(Integer.valueOf(map.get("packageId")));
            order.setOrderDate(date);
            order.setOrderType(map.get("orderType"));
            order.setOrderStatus(map.get("orderStatus"));
            Order orderByCondition = orderDao.findByCondition(order);
             if(null!=orderByCondition){
                 throw new MyException("已预约,请勿重复预约");
             }
             orderDao.addOrder(order);
             ordersettingDao.updateReservations(order);
             return order;
    }

    @Override
    public Map<String, Object> findById(int id) {
        return orderDao.findById(id);
    }


}
