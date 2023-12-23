package com.spring.rabbitmq.controller;

import com.spring.rabbitmq.producer.TransactionProducer;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auth 十三先生
 * @date 2023/12/14
 * @desc
 */
@RestController
public class SendMessageController {

    @Resource
    private TransactionProducer transactionProducer;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public String send(String message) {
        rabbitTemplate.convertAndSend("test.exchange", "test.routing", message);
        return "success";
    }

    @PostMapping("/send2")
    public String send2(String message) {
        //带有发送确认的方式。
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend("test.exchange", "test.routing", message, correlationData);
        return "success";
    }
    //发送事务消息
    @PostMapping("/send3")
    public String send3(String message) {
        //带有发送确认的方式。

        transactionProducer.sendMessage(message);
        return "success";
    }

}
