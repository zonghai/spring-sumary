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

    //一般消费者的方法都是空
    // 方法返回值不为空报错 Cannot determine ReplyTo message property value: Request message does not contain reply-to property, and no default response Exchange was set
    // 有返回值的情况，一般都是在同步调用的时候会是这样。参考 /sync
    @RabbitListener(queues = Constants.QUEUE)
    public void direct(Channel channel, Message message) {
        try {
            logger.info("queue ={}, msg={},redeliver={}", Constants.QUEUE, new String(message.getBody()), message.getMessageProperties().getRedelivered());
            //消费消息是，若抛出异常，不进行处理，并nack时 requeue =true，消息回到队列头部，可以 ack 重新发消息到队列的尾部
//            int a = 1 / 0;
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
