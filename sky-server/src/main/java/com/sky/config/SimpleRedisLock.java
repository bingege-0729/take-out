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

    private static final String LOCK_PREFIX = "lock:";
    private static final ThreadLocal<String> LOCK_THREAD_LOCAL = new ThreadLocal<>();

    public boolean tryLock(String key, long time, TimeUnit unit) {
        String uuid = UUID.randomUUID().toString();
        String lockKey = LOCK_PREFIX + key;
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, uuid, time, unit);
        if (BooleanUtils.isTrue(flag)) {
            LOCK_THREAD_LOCAL.set(uuid);
            return true;
        }
        return false;
    }

    public void unlock(String key) {
        String lockKey = LOCK_PREFIX + key;
        String uuid = LOCK_THREAD_LOCAL.get();
        if (uuid != null) {
            String currentValue = stringRedisTemplate.opsForValue().get(lockKey);
            if (uuid.equals(currentValue)) {
                stringRedisTemplate.delete(lockKey);
            }
            LOCK_THREAD_LOCAL.remove();
        }
    }

    @Deprecated
    public boolean tryLock(String key, String value, long time, TimeUnit unit) {
        return tryLock(key, time, unit);
    }
}
