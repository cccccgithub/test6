package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CheckGroupDao {

    @Select("select * from t_checkitem ")
    List<CheckItem> findCheck();

    @Insert(" insert into t_checkgroup(code,name,helpCode,sex,remark,attention) values(#{code},#{name},#{helpCode},#{sex},#{remark},#{attention})")
    @SelectKey(keyColumn = "id",keyProperty = "id",before = false,statement = "select LAST_INSERT_ID()", resultType = Integer.class)
    void add(CheckGroup checkGroup);

    @Insert(" insert into t_checkgroup_checkitem values (#{checkGroupId},#{checkitemId})")
    void addRelation(@Param("checkGroupId") Integer checkGroupId,@Param("checkitemId") int checkitemId);

    Page<CheckGroup> findGroup(String queryPageBean);

    @Select("select * from t_checkgroup where id=#{id}")
    CheckGroup showone(int id);

    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{id}")
    int[] findIds(int id);

    @Update("update t_checkgroup set code=#{code}, name=#{name}, helpCode=#{helpCode}, sex=#{sex}, remark=#{remark}, attention=#{attention} where id=#{id}")
    void updategroup(CheckGroup checkGroup);

    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{id}")
    void deleteold(Integer id);
}
