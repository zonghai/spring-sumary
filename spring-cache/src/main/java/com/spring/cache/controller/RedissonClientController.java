package com.spring.cache.controller;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @auth 十三先生
 * @date 2024/1/7
 * @desc
 */
@RestController
public class RedissonClientController {

    @Resource
    private RedissonClient redissonClient;


    @GetMapping("/init")
    public String init() {

        RRateLimiter limiter = redissonClient.getRateLimiter("xx");
        limiter.setRate(RateType.OVERALL, 1, 1000, RateIntervalUnit.MILLISECONDS);
        return "success";

    }

    @GetMapping("/limit")
    public String limit() {
        RRateLimiter limiter = redissonClient.getRateLimiter("xx");
        boolean acquire = limiter.tryAcquire();
        return "success" + acquire;
    }
}
