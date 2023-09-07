package com.example.demo.configs;

import com.example.demo.Services.RedisDetailsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class JedisConfiguration {
    @Autowired
    protected RedisDetailsConfig redisDetailsConfig;

    @Bean
    public JedisPooled initializeJedis() {
        return new JedisPooled(redisDetailsConfig.getHost(), redisDetailsConfig.getPort());
    }
}
