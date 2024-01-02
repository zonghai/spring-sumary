package com.spring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 声明惰性队列方式：
 * 增加args x-queue-mode=lazy
 * 接收到消息后直接存入磁盘而非内存
 * 消费者要消费消息时才会从磁盘中读取并加载到内存
 * 支持数百万条的消息存储
 *
 * @auth 十三先生
 * @date 2024/1/2
 * @desc
 */
@Component
public class LazyQueueMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(LazyQueueMessageListener.class);

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = Constants.LAZY_MSG_QUEUE, durable = "true", autoDelete = "false", arguments = {@Argument(name = "x-queue-mode", value = "lazy")}),
            exchange = @Exchange(value = Constants.LAZY_MSG_EXCHANGE),  key = Constants.LAZY_MSG_ROUTING)},concurrency = "1-1",ackMode = "MANUAL")
    public void lazyQueue(Channel channel, Message message) {
        try {
            logger.info("queue ={}, msg={}", Constants.LAZY_MSG_QUEUE, new String(message.getBody()));
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
