package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import com.itheima.util.QiNiuUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    @PostMapping("/getPackage")
    public Result getPackage (){
        List<Package> packages=packageService.getPackage();
        for (Package aPackage : packages) {
            aPackage.setImg("http://"+ QiNiuUtil.DOMAIN+"/"+aPackage.getImg());
        }
        return new Result(true, MessageConstant.GET_PACKAGE_LIST_SUCCESS,packages);
    }
    
    @PostMapping("/findById")
    public Result findById (int id){
      Package pkg = packageService.findById(id);
        pkg.setImg("http://"+ QiNiuUtil.DOMAIN+"/"+pkg.getImg());
        return new Result(true, MessageConstant.GET_PACKAGE_LIST_SUCCESS,pkg);
    }

}
