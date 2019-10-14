package com.itheima.dao;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    User findUserByUsername(String username);
}
