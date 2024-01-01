package com.spring.rabbitmq.producer;

import com.spring.rabbitmq.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @auth 十三先生
 * @date 2024/1/1
 * @desc
 */
@Component
public class DelayProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发消息时，设置延时时间
     *
     * @param message
     */
    public void sendDelayMessage(String message) {
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("x-delay", 10000);
                return message;
            }
        };
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.DELAY_MSG_EXCHANGE, Constants.DELAY_MSG_ROUTING, message, messagePostProcessor, correlationData);
    }
}
