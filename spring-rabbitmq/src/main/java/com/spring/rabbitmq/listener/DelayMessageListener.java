package com.spring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 延时消息 监听
 *
 * @auth 十三先生
 * @date 2024/1/1
 * @desc
 */
@Component
public class DelayMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DelayMessageListener.class);

    @RabbitListener(queues = Constants.DELAY_MSG_QUEUE, ackMode = "MANUAL")
    public void ttlTest(Channel channel, Message message) {
        try {
            logger.info("queue ={}, msg={}", Constants.DELAY_MSG_QUEUE, new String(message.getBody()));
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
