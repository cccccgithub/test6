package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.exception.MyException;
import com.itheima.util.SMSUtils;
import com.itheima.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order (String telephone) {
        Jedis jedis = jedisPool.getResource();
        String key =telephone+ RedisMessageConstant.SENDTYPE_ORDER;
        String value = jedis.get(key);
        if (null!=value) {
            return new Result(false,MessageConstant.SENT_VALIDATECODE);
        }
        try {
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
            jedis.setex(key,5*60,code+"");
            return  new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
    }
    
    @PostMapping("/send4Login")
    public Result send4Login (String telephone){
        Jedis jedis = jedisPool.getResource();
        String key =telephone+ RedisMessageConstant.SENDTYPE_LOGIN;
        String value = jedis.get(key);
        if (null!=value) {
            return new Result(false,MessageConstant.SENT_VALIDATECODE);
        }
        try {
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
            jedis.setex(key,5*60,code+"");
            return  new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);

    }
}
