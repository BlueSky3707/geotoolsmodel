package com.geotools.gistools.redisUtils;

import com.geotools.gistools.beans.Redis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/10/26 14:38
 */
@Component
public class RedisServiceUtils implements RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public Object get(final String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入数据
     */
    @Override
    public Boolean add(Redis redis) {
        try {
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            ops.set(redis.getKey(), redis.getValue());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * 是否存在摸个key
     */
    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
    /**
     * 设置自动销毁时间
     */
    @Override
    public Boolean expire(String key, int seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }
    /**
     * 删除key
     */
    @Override
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }
    /**
     * 添加数据保存到redis中并设置过期时间，单位为秒
     */
    @Override
    public Boolean addForExpireTime(Redis redis) {
        try {
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            ops.set(redis.getKey(), redis.getValue(), redis.getSeconds(),TimeUnit.SECONDS);

        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
