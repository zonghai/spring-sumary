package com.spring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 单个消费者，可以解决顺序消费的问题
 *
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
    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = Constants.SINGLE_QUEUE, durable = "true", autoDelete = "false", arguments = {@Argument(name = "x-single-active-consumer", value = "true", type = "java.lang.Boolean")}),
            exchange = @Exchange(value = Constants.SINGLE_EXCHANGE), key = Constants.SINGLE_ROUTING)}, concurrency = "5", ackMode = "MANUAL")
    public void configTest(Channel channel, Message message) {
        try {
            logger.info("queue = single.test, msg={}", new String(message.getBody()));
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
