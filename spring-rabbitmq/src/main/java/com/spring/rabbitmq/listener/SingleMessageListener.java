package com.spring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @auth 十三先生
 * @date 2023/12/26
 * @desc
 */
@Component
public class SingleMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(SingleMessageListener.class);

    /**
     * 单活消费者
     * 支持自动声明绑定，声明之后自动监听队列的队列，此时@RabbitListener注解的queue和bindings不能同时指定，否则报错
     * 单活消费者 arguments = {@Argument(name = "x-single-active-consumer", value = "true", type = "java.lang.Boolean")}
     */
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "single.test", durable = "true", autoDelete = "false", arguments = {@Argument(name = "x-single-active-consumer", value = "true", type = "java.lang.Boolean")}), exchange = @Exchange(value = "single.test.exchange"), key = "single.test.routing")}, concurrency = "5", ackMode = "MANUAL")
    public void configTest(Channel channel, Message message) {
        try {
            logger.info("queue = test3, msg={}", new String(message.getBody()));
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
