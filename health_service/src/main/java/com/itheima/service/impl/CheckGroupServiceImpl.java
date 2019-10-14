package com.itheima.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public List<CheckItem> findCheck() {
        return checkGroupDao.findCheck();
    }

    @Transactional
    @Override
    public void add(CheckGroup checkGroup, int[] checkitemIds) {
        checkGroupDao.add(checkGroup);

        if(null!=checkitemIds){
            for (int checkitemId : checkitemIds) {

                checkGroupDao.addRelation(checkGroup.getId(),checkitemId);

            }
        }
    }

    @Override
    public PageResult findGroup(QueryPageBean queryPageBean) {
         if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
             queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
         }

         PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
         Page<CheckGroup> page=checkGroupDao.findGroup(queryPageBean.getQueryString());
        PageResult<CheckGroup> pageResult = new PageResult<>(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public CheckGroup showone(int id) {
        return checkGroupDao.showone(id);
    }

    @Override
    public int[] findIds(int id) {
        return checkGroupDao.findIds(id);
    }

    @Override
    public void updategroup(CheckGroup checkGroup, int[] checkitemIds) {
        //更新检查组
        checkGroupDao.updategroup(checkGroup);
        //删除已有关系
        checkGroupDao.deleteold(checkGroup.getId());
        //增加新关系
        for (int checkitemId : checkitemIds) {
            checkGroupDao.addRelation(checkGroup.getId(),checkitemId);
        }

    }


}
