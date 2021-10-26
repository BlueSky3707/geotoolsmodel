package com.geotools.gistools.redisUtils;

import com.geotools.gistools.beans.Redis;

/**
 * 功能描述：
 *
 * @Author: ddw
 * @Date: 2021/10/26 11:00
 */
public interface RedisService {
    /**
     * 获取值
     *
     * @param key 存储数据的键
     * @return 获取到存储数据的键的数据值
     */
    Object get(String key);

    /**
     * 设置内容
     *
     * @param redis 存储数据的键
     * @return 保存成功与否
     */
    Boolean add(Redis redis);

    /**
     * 到时自动销毁
     *
     * @param key 存储数据的键
     * @param seconds 过期时间多少秒
     */
    Boolean expire(String key, int seconds);

    /**
     * 判断key是否存在
     *
     * @param key 存储数据的键
     * @return 是否存在该键的数据值
     */
    Boolean exists(String key);

    /**
     *
     * @param redis 存储数据的键
     * @return
     */
    Boolean addForExpireTime(Redis redis);

    /**
     *
     * @param key 存储数据的键
     * @return 删除成功与否
     */
    Boolean delete(String key);
}
