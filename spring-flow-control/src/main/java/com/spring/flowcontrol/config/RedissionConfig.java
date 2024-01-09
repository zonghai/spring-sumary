package com.spring.flowcontrol.config;

import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auth 十三先生
 * @date 2024/1/9
 * @desc
 */
@Configuration
public class RedissionConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient getRedisson() {
        Config config = new Config();
        config.useSingleServer().
                setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .setPassword(redisProperties.getPassword())
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(5)
                .setTimeout(200)
        ;
        config.setCodec(new JsonJacksonCodec());
        return Redisson.create(config);
    }
}
