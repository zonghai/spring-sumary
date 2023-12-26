package com.spring.rabbitmq.controller;

import com.spring.rabbitmq.constant.Constants;
import com.spring.rabbitmq.producer.TransactionProducer;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
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

    @PostMapping("/single")
    public String send2(String message) {
        //带有发送确认的方式。
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.SINGLE_EXCHANGE, Constants.SINGLE_ROUTING, message, correlationData);
        return "success";
    }

    //发送事务消息
    @PostMapping("/transaction")
    public String send3(String message) {
        //带有发送确认的方式。

        transactionProducer.sendMessage(message);
        return "success";
    }

    @PostMapping("/direct")
    public String send4(String message) {
        //带有发送确认的方式。
        //带有发送确认的方式。
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.EXCHANGE, Constants.ROUTING, message, correlationData);
        return "success";
    }

    @PostMapping("/fanout")
    public String fanout(String message) {
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.FANOUT_EXCHANGE, "", message, correlationData);
        return "success";
    }

    @PostMapping("/topic")
    public String topic(String message) {
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.TOPIC_EXCHANGE, "topic.test." + message, message, correlationData);
        return "success";
    }

    @PostMapping("/header")
    public String header(String message) {
        Message nameMsg = MessageBuilder.withBody(message.getBytes()).setHeader("name", "aaa").build();
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.HEADER_EXCHANGE, "" + message, nameMsg, correlationData);
        return "success";
    }


}
