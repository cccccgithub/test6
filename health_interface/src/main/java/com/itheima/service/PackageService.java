package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Package;

import java.util.List;

public interface PackageService {
    List<CheckGroup> findAllGroups();

    void addPackage(Package mypackage, int[] ids);

    PageResult<Package> findPage(QueryPageBean queryPageBean);

    List<Package> getPackage();

    Package findById(int id);
}
