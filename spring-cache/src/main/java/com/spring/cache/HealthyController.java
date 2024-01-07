package com.spring.cache;

import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @auth 十三先生
 * @date 2024/1/6
 * @desc
 */
@RestController
public class HealthyController {

//    @Resource
//    private CacheManager cacheManager;
    @Resource
    private RedissonClient redissonClient;

    @GetMapping("/healthy")
    public String healthy() {
//        cacheManager.getCacheNames();
//        Cache book = cacheManager.getCache("books");
//        book.put("xxx","xxxxx");
//        Config config = redissonClient.getConfig();
//        return redissonClient.getId() + cacheManager.getCacheNames();

        return "11";
    }
}
