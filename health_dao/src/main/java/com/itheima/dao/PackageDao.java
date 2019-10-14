package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Package;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

public interface PackageDao {
    @Select("select * from t_checkgroup")
    List<CheckGroup> findAllGroups();

    @SelectKey(keyProperty = "id",keyColumn = "id",before = false,resultType = Integer.class,statement = "select LAST_INSERT_ID()")
    @Insert("insert into t_package (name,code,helpCode,sex,age,price,remark,attention,img) values (#{name},#{code},#{helpCode},#{sex},#{age},#{price},#{remark},#{attention},#{img})")
    void addPackage(Package mypackage);


    @Insert("insert into t_package_checkgroup values(#{packageId},#{checkgroupId})")
    void addGroupRelation(@Param("packageId") Integer id, @Param("checkgroupId") int id1);

    Page<Package> findPackage(String queryString);

    @Select("select * from t_package")
    List<Package> getPackage();


    Package findById(int id);
}
