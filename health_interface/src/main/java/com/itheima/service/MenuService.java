package com.itheima.service;

import com.itheima.pojo.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> getMenuList(String username);

    List<Menu> getChildren(Integer pid);
}
