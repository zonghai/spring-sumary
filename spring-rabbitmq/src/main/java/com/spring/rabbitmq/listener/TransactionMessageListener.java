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
 * @auth 十三先生
 * @date 2024/1/3
 * @desc
 */
@Component
public class TransactionMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionMessageListener.class);

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(value = Constants.TRANSACTION_QUEUE, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = Constants.TRANSACTION_EXCHANGE), key = Constants.TRANSACTION_ROUTING)}, concurrency = "1-2", ackMode = "MANUAL")
    public void test(Channel channel, Message message) {
        try {
            logger.info("queue = {}, msg={}", Constants.TRANSACTION_QUEUE, new String(message.getBody()));
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
