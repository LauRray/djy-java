package com.djy.manager.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class RedisUtil {

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 清楚key
     */
    public void deleteKeys(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
