package com.spring.rabbitmq.producer;

import com.spring.rabbitmq.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 带有事务的消息生产者
 *
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
        //捕获异常不抛出，消息会发送成功的。
        try {
            CorrelationData correlationData = new CorrelationData(message);
            rabbitTemplate.convertAndSend(Constants.TRANSACTION_EXCHANGE, Constants.TRANSACTION_ROUTING, message, correlationData);
            throw new RuntimeException("异常了");
        } catch (Exception e) {

        }
    }
}
