package com.example.demo.Repository;

import com.example.demo.Services.RedisDetailsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.JedisPooled;
@Repository
public class CacheRepository implements ICacheRepository{
    @Autowired
    JedisPooled jedisPool;

    @Autowired
    private RedisDetailsConfig redisDetailsConfig;

    @Override
    public void createCacheEntity(String key, String value) {
        jedisPool.setex(key, redisDetailsConfig.getTtl(), value);
    }

    @Override
    public String getCacheEntity(String key) {
        if (isKeyExist(key)){
            return jedisPool.get(key);
        }
        return null;
    }

    @Override
    public void updateCacheEntity(String key, String value) {
        if (isKeyExist(key)){
            jedisPool.setex(key, redisDetailsConfig.getTtl(), value);
        }
    }

    @Override
    public Boolean isKeyExist(String key) {
        return jedisPool.exists(key);
    }

    @Override
    public void removeKey(String key) {
        if (isKeyExist(key)) {
            jedisPool.del(key);
        }
    }

    @Override
    public void setKeyExpiration(String key, Integer seconds) {
        jedisPool.expire(key, seconds);
    }

}
