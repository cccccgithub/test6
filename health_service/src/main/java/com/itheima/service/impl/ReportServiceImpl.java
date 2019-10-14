package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.PackageDao;
import com.itheima.pojo.Member;
import com.itheima.service.ReportService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Map<String, Object> getBusinessReportData() {

        Map<String,Object> resultData = new HashMap<>();

        //今日日期
        String today = DateUtils.date2String(DateUtils.getToday(), DateUtils.YMD);
        //本周第一天
        String Monday = DateUtils.date2String(DateUtils.getThisWeekMonday(), DateUtils.YMD);
        //本周最后一天
        String Sunday = DateUtils.date2String(DateUtils.getSundayOfThisWeek(), DateUtils.YMD);
        //本月第一天
        String firstDayOfMonth = DateUtils.date2String(DateUtils.getFirstDayOfThisMonth(), DateUtils.YMD);
        //本月最后一天
        String lastDayOfMonth = DateUtils.date2String(DateUtils.getLastDayOfThisMonth(), DateUtils.YMD);


        //reportDate:null, 日期
        resultData.put("reportDate",today);
        //todayNewMember :0, 本日新增会员数
        Integer todayNewMember = memberDao.findMemberCountAfterDate(today);
        resultData.put("todayNewMember",todayNewMember);
        //totalMember :0, 总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        resultData.put("totalMember",totalMember);
        //thisWeekNewMember :0, 本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(Monday);
        resultData.put("thisWeekNewMember",thisWeekNewMember);
        //thisMonthNewMember :0, 本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfMonth);
        resultData.put("thisMonthNewMember",thisMonthNewMember);
        //todayOrderNumber:0, 今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountAfterDate(today);
        resultData.put("todayOrderNumber",todayOrderNumber);
        //todayVisitsNumber :0, 今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountAfterDate(today);
        resultData.put("todayVisitsNumber",todayVisitsNumber);
        //thisWeekOrderNumber :0, 本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(Monday,Sunday);
        resultData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        //thisWeekVisitsNumber :0, 本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(Monday);
        resultData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        //thisMonthOrderNumber :0, 本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfMonth, lastDayOfMonth);
        resultData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        //thisMonthVisitsNumber :0, 本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfMonth);
        resultData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        //hotPackage :[ 	{name: 套餐名称,count:预约数量,proportion:占比,remark:备注},
        //                  {name: 套餐名称,count:预约数量,proportion:占比,remark:备注} ]
        List<Map<String, Object>> hotPackage = orderDao.findHotPackage();
        resultData.put("hotPackage",hotPackage);

        return resultData;
    }

    @Override
    public List<Map<String, Object>> getPackageReport() {
        return orderDao.getPackageReport();
    }

    @Override
    public List<Map<String, Object>> getMemberSexReport() {
        List<Map<String, Object>> memberSexReport = memberDao.getMemberSexReport();

        for (Map<String, Object> maps : memberSexReport) {
            String s = (String) maps.get("name");
            int sex= Integer.parseInt(s);
            if(sex==1){
                maps.put("name","男");
                 }else{
                maps.put("name","女");
            }
        }

        return memberSexReport;
    }

    @Override
    public List<Map<String, Object>> getMemberAge() {
        //返回的是list,list中的map有两个值.key分别为"name"和"value"
        //数据库的birthday为Date类型,写出startBirth
        //select count(1) from t_member where birthday > #{startBirth}
        //SELECT COUNT(1) FROM t_member WHERE birthday > '1920-01-01' 可以查到大于此字符串的会员的数量

        List<Date> dates = memberDao.findBirthday();
        int  count18=0;
        int  count30=0;
        int  count45=0;
        int  count45up=0;

        for (Date date : dates) {
            int age = DateUtils.getAgeByBirth(date);
             if(age<=18){
                 count18++;
             }else{
                  if(age <= 30){
                      count30++;
                      }else{
                       if(age<=45){
                           count45++;
                           }else{
                           count45up++;
                       }
                  }
             }
        }

        List<Map<String, Object>> list = new ArrayList();
         Map<String,Object> map1 = new HashMap<>();
         map1.put("name","age<=18");
         map1.put("value",count18);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","18<age<=30");
        map2.put("value",count30);

        Map<String,Object> map3 = new HashMap<>();
        map3.put("name","30<age<=45");
        map3.put("value",count45);

        Map<String,Object> map4 = new HashMap<>();
        map4.put("name","45<age");
        map4.put("value",count45up);

        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);

        return list;
    }
}
