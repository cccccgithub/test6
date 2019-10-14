package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface CheckItemDao {

    @Insert("insert into t_checkitem (code,name,sex,age,price,type,remark,attention) values (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})")
    void add(CheckItem checkItem);

    Page<CheckItem> findAllByCondition(String queryString);

    @Select("select count(0) from t_checkgroup_checkitem where checkitem_id=#{id}")
    int findCountByCheckItemId(int id);

    @Delete("delete from t_checkitem where id=#{id}")
    void delete(int id);

    @Select("select * from t_checkitem where id=#{id}")
    CheckItem findById(int id);

    @Update("update t_checkitem set code=#{code},name=#{name},sex=#{sex},age=#{age},price=#{price},type=#{type},remark=#{remark},attention=#{attention} where id=#{id}")
    void update(CheckItem checkItem);
}
