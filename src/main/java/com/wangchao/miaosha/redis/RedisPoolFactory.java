package com.wangchao.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {

    @Autowired
    private RedisCongfig redisCongfig;

    @Bean
    public JedisPool jedisPoolFactory(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisCongfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisCongfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisCongfig.getPoolMaxWait() * 1000);
        JedisPool jp = new JedisPool(poolConfig,redisCongfig.getHost(),
                redisCongfig.getPort(),redisCongfig.getTimeout() * 1000);
        return jp;
    }
}
