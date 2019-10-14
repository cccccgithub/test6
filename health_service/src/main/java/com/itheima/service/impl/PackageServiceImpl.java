package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.dao.PackageDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = PackageService.class)
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageDao packageDao;
    @Override
    public List<CheckGroup> findAllGroups() {
        return packageDao.findAllGroups();
    }

    @Transactional
    @Override
    public void addPackage(Package mypackage, int[] ids) {
        packageDao.addPackage(mypackage);
        for (int id : ids) {
            packageDao.addGroupRelation(mypackage.getId(),id);
        }

    }

    @Override
    public PageResult<Package> findPage(QueryPageBean queryPageBean) {
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Package> page=packageDao.findPackage(queryPageBean.getQueryString());
        return new PageResult<Package>(page.getTotal(),page.getResult());

    }

    @Override
    public List<Package> getPackage() {
        return packageDao.getPackage();
    }

    @Override
    public Package findById(int id) {
        return packageDao.findById(id);
    }
}
