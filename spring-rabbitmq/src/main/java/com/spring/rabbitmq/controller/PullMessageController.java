package com.spring.rabbitmq.controller;

import com.spring.rabbitmq.listener.PullMessageListener;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auth 十三先生
 * @date 2023/12/15
 * @desc
 */
@RestController
public class PullMessageController {
    @Resource
    private PullMessageListener pullMessageListener;


    @GetMapping("/pull")
    public String pullMessage(String queue) {

        return pullMessageListener.pullMessage(queue) + queue;
    }
}
