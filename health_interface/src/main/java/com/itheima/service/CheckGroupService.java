package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckGroupService {
    List<CheckItem> findCheck();

    void add(CheckGroup checkGroup, int[] checkitemIds);


    PageResult findGroup(QueryPageBean queryPageBean);

    CheckGroup showone(int id);

    int[] findIds(int id);

    void updategroup(CheckGroup checkGroup, int[] checkitemIds);
}
