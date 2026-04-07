package com.sky.config;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class SimpleRedisLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
    * 尝试获取锁的方法
    * @param key 锁的键
    * @param value 锁的值
    * @param time 锁的过期时间
    * @param unit 时间单位
    * @return 如果获取锁成功返回true，否则返回false
    */
    public boolean tryLock(String key, String value, long time, TimeUnit unit){

    // 使用Redis的setIfAbsent方法尝试设置键值对，并设置过期时间
    // 如果键不存在，则设置成功并返回true；如果键已存在，则设置失败并返回false
        //生成一个随机的UUID，作为锁的值
        String uuid = UUID.randomUUID().toString();
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key+uuid,value,time,unit);

        //boolean和Boolean两个是不同类型，一个基本类型，一个引用类型，如果直接
        //返回flag会直接拆箱有空指针的隐患，所以需要用BooleanUtils.isTrue()转换一下
        return BooleanUtils.isTrue(flag);
    }


    /**
     * 解锁方法
     * 根据给定的键从Redis中删除对应的值
     * @param key 要删除的键，用于标识需要解锁的资源
     */
    public void unlock(String key){

        // 使用StringRedisTemplate删除指定key的数据
        stringRedisTemplate.delete(key);
    }
}
