package com.spring.rabbitmq.listener;


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
public class MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "test", durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = "test.exchange"), key = "test.routing")}, concurrency = "2-5", ackMode = "MANUAL")
    public void test(Channel channel, Message message) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            logger.info("queue = test, msg={}", new String(message.getBody()));
            channel.basicAck(deliveryTag, Boolean.FALSE);
        } catch (Exception e) {
            try {
                channel.basicNack(deliveryTag, Boolean.FALSE, true);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 单活消费者
     * 支持自动声明绑定，声明之后自动监听队列的队列，此时@RabbitListener注解的queue和bindings不能同时指定，否则报错
     * 单活消费者 arguments = {@Argument(name = "x-single-active-consumer", value = "true", type = "java.lang.Boolean")}
     *
     * @param msg
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "test3", durable = "true", autoDelete = "false", arguments = {@Argument(name = "x-single-active-consumer", value = "true", type = "java.lang.Boolean")}),
            exchange = @Exchange(value = "test.exchange3"), key = "test.routing3")}, concurrency = "5", ackMode = "MANUAL")
    public void test3(String msg) {
        System.out.println(msg);
    }


}
