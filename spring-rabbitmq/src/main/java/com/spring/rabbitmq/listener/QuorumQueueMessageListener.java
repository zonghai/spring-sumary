package com.spring.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 仲裁队列 消息监听
 * 给队列设置参数 x-queue-type 值为 quorum 就可将队列变为仲裁队列
 *
 * @auth 十三先生
 * @date 2024/1/1
 * @desc
 */
@Component
public class QuorumQueueMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(QuorumQueueMessageListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = Constants.QUORUM_MSG_QUEUE,
                    //给队列设置参数 x-queue-type 值为 quorum 就可将队列变为仲裁队列
                    arguments = @Argument(name = "x-queue-type", value = "quorum")),
            exchange = @Exchange(name = Constants.QUORUM_MSG_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = Constants.QUORUM_MSG_ROUTING
    ))
    public void quorumTest(Channel channel, Message message) {
        try {
            logger.info("queue ={}, msg={}", Constants.QUORUM_MSG_QUEUE, new String(message.getBody()));
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
