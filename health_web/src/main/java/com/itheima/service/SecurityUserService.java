package com.itheima.service;


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("securityUserSerivce")
public class SecurityUserService implements UserDetailsService{

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        User user= userService.findUserByUsername(username);

        if (null!=user) {
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

            Set<Role> roles = user.getRoles();
            GrantedAuthority authority =null;
            if (null!=roles) {
                for (Role role : roles) {
                     authority = new SimpleGrantedAuthority(role.getKeyword());
                    grantedAuthorities.add(authority);
                    Set<Permission> permissions = role.getPermissions();
                    if (null!=permissions) {
                        for (Permission permission : permissions) {
                            authority = new SimpleGrantedAuthority(permission.getKeyword());
                            grantedAuthorities.add(authority);
                        }
                    }
                }
            }

            org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(username,user.getPassword(),grantedAuthorities);
            return user1;
        }
        return null;
    }
}
