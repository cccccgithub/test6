package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.util.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private ReportService reportService;

    
    @GetMapping("/getMemberReport")
    public Result getMemberReport (){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        Map<String,Object> map = new HashMap<>();
        ArrayList<String> months = new ArrayList();
        ArrayList<Integer> memberCount = new ArrayList();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            String month = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            months.add(month);

            Integer count= memberService.findMemberCountBeforeDate(month);
            memberCount.add(count);

        }

        map.put("months",months);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @GetMapping("/getPackageReport")
    public Result  getPackageReport(){
        List<Map<String,Object>> packageCount= reportService.getPackageReport();
         Map<String,Object> map = new HashMap<>();
         ArrayList<Object> packageNames = new ArrayList();
         map.put("packageCount",packageCount);
        for (Map<String, Object> map1 : packageCount) {
            packageNames.add(map1.get("name"));
        }
        map.put("packageNames",packageNames);
        return new Result(true,MessageConstant.GET_PACKAGE_COUNT_REPORT_SUCCESS,map);
    }

    @GetMapping("/getBusinessReportData")
    public Result  getBusinessReportData(){
        Map<String,Object> resultData= reportService.getBusinessReportData();
        return new Result(true,MessageConstant.GET_PACKAGE_COUNT_REPORT_SUCCESS,resultData);
        }

    //将报表数据导出
    @GetMapping("/exportBusinessReport")
    public Result exportBusinessReport (HttpServletRequest req, HttpServletResponse res){
        Map<String,Object> map= reportService.getBusinessReportData();
        //将数据写在excel中
        //获取模板
        String template= req.getSession().getServletContext().getRealPath("template")+ File.separator+ "report_template.xlsx";

        try( XSSFWorkbook wb = new XSSFWorkbook(template);) {
            //根据模板创建工作簿

            //获取工作表
            XSSFSheet sht = wb.getSheetAt(0);
            //写入内容
            sht.getRow(2).getCell(5).setCellValue(((String) map.get("reportDate")));
            // 会员数据
            sht.getRow(4).getCell(5).setCellValue(((Integer) map.get("todayNewMember")));
            sht.getRow(4).getCell(7).setCellValue(((Integer) map.get("totalMember")));
            sht.getRow(5).getCell(5).setCellValue(((Integer) map.get("thisWeekNewMember")));
            sht.getRow(5).getCell(7).setCellValue(((Integer) map.get("thisMonthNewMember")));
            // 预约数量
            sht.getRow(7).getCell(5).setCellValue(((Integer) map.get("todayOrderNumber")));
            sht.getRow(7).getCell(7).setCellValue(((Integer) map.get("todayVisitsNumber")));
            sht.getRow(8).getCell(5).setCellValue(((Integer) map.get("thisWeekOrderNumber")));
            sht.getRow(8).getCell(7).setCellValue(((Integer) map.get("thisWeekVisitsNumber")));
            sht.getRow(9).getCell(5).setCellValue(((Integer) map.get("thisMonthOrderNumber")));
            sht.getRow(9).getCell(7).setCellValue(((Integer) map.get("thisMonthVisitsNumber")));
            // 热门套餐
            int rowCnt=12;
            List<Map<String,Object>> hotPackage = (List<Map<String, Object>>) map.get("hotPackage");
            if (null!=hotPackage) {
                for (Map<String, Object> pkgMap : hotPackage) {
                    sht.getRow(rowCnt).getCell(4).setCellValue(((String) pkgMap.get("name")));
                    //Long
                    sht.getRow(rowCnt).getCell(5).setCellValue(((Long) pkgMap.get("count")));
                    //BigDecimal
                    sht.getRow(rowCnt).getCell(6).setCellValue(((BigDecimal) pkgMap.get("proportion")).toString());
                    sht.getRow(rowCnt).getCell(7).setCellValue(((String) pkgMap.get("remark")));
                    rowCnt++;
                }
            }
            //  告诉浏览器接收的是文件 Content-Type 这个的内容是excel文件
            res.setContentType("application/vnd.ms-excel");

            // 下载的文件的名字 要解决乱码,转换为字节输出流
            String filename = "运营数据.xlsx";
            filename = new String(filename.getBytes(),"ISO-8859-1");
            //调用Response的输出流实现 下载
            res.setHeader("Content-Disposition","attachment;filename=" + filename);
            ServletOutputStream out = res.getOutputStream();
            wb.write(out);
            out.flush();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
    }

    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res){
        String template = req.getSession().getServletContext().getRealPath("template") + File.separator + "report_template2.xlsx";
        // 数据模型
        Context context = new PoiContext();
        context.putVar("obj", reportService.getBusinessReportData());
        try {
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template),res.getOutputStream(),context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getMemberSex")
    public Result getMemberSex (){
        List<Map<String,Object>> memberCount= reportService.getMemberSexReport();
        Map<String,Object> map = new HashMap<>();
        ArrayList<Object> memberNames = new ArrayList();
        map.put("memberCount",memberCount);
        for (Map<String, Object> map1 : memberCount) {
            memberNames.add(map1.get("name"));
        }
        map.put("memberNames",memberNames);
        return new Result(true,MessageConstant.GET_PACKAGE_COUNT_REPORT_SUCCESS,map);
    }

    @GetMapping("/getMemberAge")
    public Result getMemberAge (){
        List<Map<String,Object>> ageCount= reportService.getMemberAge();
        Map<String,Object> map = new HashMap<>();
        ArrayList<Object> memberAges = new ArrayList();
        map.put("ageCount",ageCount);
        for (Map<String, Object> map1 : ageCount) {
            memberAges.add(map1.get("name"));
        }
        map.put("memberAges",memberAges);
        return new Result(true,MessageConstant.GET_PACKAGE_COUNT_REPORT_SUCCESS,map);
        
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        System.out.println(calendar.toString());
        //当前是十月,退十二个月,结果是YEAR=2018,MONTH=9
    }
}
