package com.itheima.dao;

import com.itheima.pojo.Menu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuDao {

    //根据姓名找出role_id
    @Select("SELECT ur.role_id FROM t_user_role ur ,t_user u WHERE u.id=ur.user_id AND username=#{username}")
    int findRoleId(String username);

    @Select("SELECT * FROM t_menu WHERE LEVEL=1 AND id IN ( SELECT menu_id ids FROM t_role_menu rm , t_menu m WHERE m.id=rm.menu_id AND rm.role_id =#{roleId}) ")
    List<Menu> findParent(int roleId);

    @Select("SELECT * FROM t_menu WHERE parentMenuId=#{pid}")
    List<Menu> findChildren(Integer pid);

}
