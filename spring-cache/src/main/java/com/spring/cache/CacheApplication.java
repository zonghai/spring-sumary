package com.spring.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


/**
 * @auth 十三先生
 * @date 2024/1/6
 * @desc
 */
@EnableCaching
@SpringBootApplication
public class CacheApplication {


    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class);
    }
}
