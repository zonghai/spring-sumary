package com.spring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 广播监听器 fanout exchange
 * 与fanout exchange绑定的每个队列都会收到一条消息，与RocketMQ不一样。
 *
 * @auth 十三先生
 * @date 2023/12/26
 * @desc
 */
@Component
public class FanoutMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(FanoutMessageListener.class);

    @RabbitListener(queues = Constants.FANOUT_QUEUE)
    public void configTest(Channel channel, Message message) {
        try {
            logger.info("queue = {}, msg={}", Constants.FANOUT_QUEUE, new String(message.getBody()));
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
