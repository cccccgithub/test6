package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import com.itheima.util.QiNiuUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/package")
public class PackageController {
    @Reference
    private PackageService packageService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/upload")
    public Result upload (@RequestParam("imgFile")MultipartFile imgFile){
        String originalFilename = imgFile.getOriginalFilename();
        String s = UUID.randomUUID().toString();
        String extend= originalFilename.substring(originalFilename.lastIndexOf("."));
        String newName=s+extend;
        try {
            QiNiuUtil.uploadViaByte(imgFile.getBytes(),newName);
            //文件上传成功
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newName);
             Map<String,Object> params = new HashMap<>();
             params.put("name",newName);
             params.put("domain",QiNiuUtil.DOMAIN);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,params);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
    
    @GetMapping("/findAllGroups")
    public Result  findAllGroups(){
       List<CheckGroup> list= packageService.findAllGroups();
       return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }

    @PostMapping("/addPackage")
    public Result addPackage (@RequestBody Package mypackage,int[] ids){
        packageService.addPackage(mypackage,ids);
        //添加成功
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,mypackage.getImg());

        return new Result(true,MessageConstant.ADD_PACKAGE_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage (@RequestBody QueryPageBean queryPageBean){
        PageResult<Package> pageResult= packageService.findPage(queryPageBean);
        return  new Result(true,MessageConstant.QUERY_PACKAGE_SUCCESS,pageResult);
        
    }
}
