package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Set;

public class JobDemo {

    @Autowired
    private JedisPool jedisPool;


    public void  run(){
      //定时任务入门  System.out.println(new Date());
        Jedis jedis = jedisPool.getResource();
        Set<String> need2Delete = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        QiNiuUtil.removeFiles(need2Delete.toArray(new String[]{}));
        jedis.del(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

    }

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
    }
}
