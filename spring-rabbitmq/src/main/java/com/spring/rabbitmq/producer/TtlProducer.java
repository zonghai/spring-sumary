package com.spring.rabbitmq.producer;

import com.spring.rabbitmq.constant.Constants;
import jakarta.annotation.Resource;
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
     * expiration 字段以微秒为单位表示 TTL 值。且与 x-message-ttl 具有相同的约束条件
     * 因为 expiration 字段必须为字符串类型，broker 将只会接受以字符串形式表达的数字
     *
     * @param message
     */
    public void sendTtlMessage(String message) {
        MessagePostProcessor messagePostProcessors = messagePostProcessor -> {
            messagePostProcessor.getMessageProperties().setExpiration("5000");
            return messagePostProcessor;
        };
        CorrelationData correlationData = new CorrelationData(message);
        rabbitTemplate.convertAndSend(Constants.TTL_MSG_EXCHANGE, Constants.TTL_MSG_ROUTING, message, messagePostProcessors, correlationData);
    }
}
