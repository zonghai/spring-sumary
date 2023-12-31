package com.spring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 发消息时 设置 消息过期时间
 * 消息过期 处理类。
 *
 * @auth 十三先生
 * @date 2023/12/30
 * @desc
 */
@Component
public class TtlMsgMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(TtlMsgMessageListener.class);

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(Constants.TTL_MSG_QUEUE), exchange = @Exchange(Constants.TTL_MSG_EXCHANGE), key = {Constants.TTL_MSG_ROUTING})}, ackMode = "MANUAL")
    public void ttlTest(Channel channel, Message message) {
        try {
            logger.info("queue ={}, msg={}", Constants.TTL_MSG_QUEUE, new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);
        } catch (Exception e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE, true);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
