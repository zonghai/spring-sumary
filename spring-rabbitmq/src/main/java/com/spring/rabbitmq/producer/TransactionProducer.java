package com.spring.rabbitmq.producer;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @auth 十三先生
 * @date 2023/12/20
 * @desc
 */
@Component
public class TransactionProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Transactional(rollbackFor = Exception.class, transactionManager = "rabbitTransactionManager")
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("test.exchange", "test.routing", message);
    }
}