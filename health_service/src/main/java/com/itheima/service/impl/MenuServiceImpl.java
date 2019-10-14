package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MenuDao;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> getMenuList(String username) {

        int roleId= menuDao.findRoleId(username);

        List<Menu> menus =menuDao.findParent(roleId);
        return menus;
    }

    @Override
    public List<Menu> getChildren(Integer pid) {
        return menuDao.findChildren(pid);
    }
}
