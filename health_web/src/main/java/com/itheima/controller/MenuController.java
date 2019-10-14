package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    @GetMapping("/getMenuList")
    public Result getMenuList (){

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        //查询出此登录者拥有的一级菜单
        List<Menu> menus= menuService.getMenuList(username);
        //遍历一级菜单
        for (Menu menu : menus) {
            Integer pid = menu.getId();
            //将一级菜单的id作为二级菜单的parentId查询出子菜单
            List<Menu> children= menuService.getChildren(pid);
            menu.setChildren(children);
        }
        //遍历并将查询结果封装到map中
        List<Map<String,Object>> resultData= new ArrayList();


        Map<String,Object> map = null;

        for (Menu menu : menus) {
          map= new HashMap<>();
          map.put("path",menu.getPath());
          map.put("title",menu.getName());
          map.put("icon",menu.getIcon());

          List<Map<String,Object>> childrenList=new ArrayList();

          List<Menu> children = menu.getChildren();
          Map<String,Object> childrenMap = null;
          List<Object> childchild = new ArrayList();
          for (Menu child : children) {
              childrenMap = new HashMap<>();
              childrenMap.put("path",child.getPath());
              childrenMap.put("title",child.getName());
              childrenMap.put("linkUrl",child.getLinkUrl());
              childrenMap.put("children",childchild);

              childrenList.add(childrenMap);
          }

          map.put("children",childrenList);
          resultData.add(map);

        }


        return new Result(true, MessageConstant.GET_MENU_SUCCESS,resultData);
    }

}
