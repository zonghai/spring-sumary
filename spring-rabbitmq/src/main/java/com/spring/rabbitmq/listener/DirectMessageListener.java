package com.spring.rabbitmq.listener;


import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * RabbitMQ主动推送的方式。
 *
 * @auth 十三先生
 * @date 2023/12/10
 * @desc
 */
@Component
public class DirectMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(DirectMessageListener.class);

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "test", durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = "test.exchange"), key = "test.routing")}, concurrency = "2-5", ackMode = "MANUAL")
    public void test(Channel channel, Message message) {
        try {
            logger.info("queue = test, msg={}", new String(message.getBody()));
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);
        } catch (Exception e) {
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE, true);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    @RabbitListener(queues = Constants.QUEUE)
    public void configTest(Channel channel, Message message) {
        try {
            logger.info("queue ={}, msg={}", Constants.QUEUE, new String(message.getBody()));
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
