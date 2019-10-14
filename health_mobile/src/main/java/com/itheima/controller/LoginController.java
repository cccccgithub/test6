package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;

import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @PostMapping("/check")
    public Result check (@RequestBody Map<String,String> map, HttpServletResponse res){
        Jedis jedis = jedisPool.getResource();
        String key =map.get("telephone")+ RedisMessageConstant.SENDTYPE_LOGIN;
        String value = jedis.get(key);
        if (StringUtils.isEmpty(value)) {
            return new Result(false,"请重新获取验证码");
        }

        if (!value.equals(map.get("validateCode"))) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        jedis.del(key);

        Member member= memberService.findByTelephone(map.get("telephone"));
        if (null==member) {
            Member member1 = new Member();
            member1.setRegTime(new Date());
            member1.setPhoneNumber(map.get("telephone"));
            memberService.add(member1);
        }

        Cookie cookie = new Cookie("telephoneNmumber",map.get("telephone"));
        cookie.setMaxAge(60*60*24*30);
        cookie.setPath("/");
        res.addCookie(cookie);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);


    }



}
