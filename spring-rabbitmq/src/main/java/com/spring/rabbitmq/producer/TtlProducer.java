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
 * @date 2023/12/31
 * @desc
 */
@Component
public class TtlProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发消息时，设置消息过期时间
     *
     * @param message
     */
    public void sendTtlMessage(String message) {
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("5000");
                return message;
            }
        };
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.TTL_MSG_EXCHANGE, Constants.TTL_MSG_ROUTING, message, messagePostProcessor, correlationData);
    }
}
