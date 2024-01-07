package com.spring.cache.controller;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @auth 十三先生
 * @date 2024/1/7
 * @desc
 */
@RestController
public class EhcacheController {

    @Resource
    private CacheManager cacheManager;


    @GetMapping("/ehcache")
    public String ehcache() {
        Cache books = cacheManager.getCache("books");
        if (books != null) {
            books.put("xx", "xx");
        }
        return books.get("xx").get().toString();
    }
}
