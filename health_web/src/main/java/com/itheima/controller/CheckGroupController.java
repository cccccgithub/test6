package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @GetMapping("/findCheck")
    public Result findCheck(){
        List<CheckItem> list= checkGroupService.findCheck();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }

    @PostMapping("/add")
    public Result add (@RequestBody CheckGroup checkGroup,int[] checkitemIds){
        checkGroupService.add(checkGroup,checkitemIds);
        return  new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @PostMapping("/findGroup")
    public Result findGroup (@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=checkGroupService.findGroup(queryPageBean);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }
    
    @GetMapping("/showone")
    public Result showone (int id){
       CheckGroup checkGroup= checkGroupService.showone(id);
       int[] ids= checkGroupService.findIds(id);
        Map<String,Object> params = new HashMap<>();
        params.put("checkGroup",checkGroup);
        params.put("ids",ids);
       return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,params);
    }
    
    @PostMapping("/updategroup")
    public Result updategroup (@RequestBody CheckGroup checkGroup,int[] checkitemIds){
        checkGroupService.updategroup(checkGroup,checkitemIds);
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }
}
