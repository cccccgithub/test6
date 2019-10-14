package com.itheima.jobs;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.dao.OrdersettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrdersettingService;
import com.itheima.util.QiNiuUtil;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class uploadjob {

    @Autowired
    private OrdersettingDao ordersettingDao;

    public void  run() throws IOException {

        //从数据库获取预定表的内容
        List<OrderSetting> orderSettingList= ordersettingDao.getOrderSetting();

        //1 创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        //2 创建工作表对象
        XSSFSheet xssfSheet = workbook.createSheet("预定数量表");
        //3 创建行
        XSSFRow row01 = xssfSheet.createRow(0);
        //4 创建列,设置内容

        row01.createCell(1).setCellValue("预约日期");
        row01.createCell(2).setCellValue("可预约数量");

        int rowCnt=1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (OrderSetting orderSetting : orderSettingList) {
            XSSFRow row02 = xssfSheet.createRow(rowCnt);

            row02.createCell(1).setCellValue(sdf.format(orderSetting.getOrderDate()));
            row02.createCell(2).setCellValue(orderSetting.getNumber());
            rowCnt++;
        }
        //5.通过输出流对象写到七牛云
        String str = UUID.randomUUID().toString();
        //5.通过输出流对象写到磁盘
        Date date = new Date();
        String newdate = sdf.format(date);
        FileOutputStream os = new FileOutputStream("D:/sss/"+str+".xlsx");
        workbook.write(os);
        os.flush();
        os.close();

        String fileName = newdate+"_"+str+".xlsx";
        QiNiuUtil.uploadFile("D:/sss/"+str+".xlsx",fileName);
        workbook.close();
    }

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
    }


}
