package com.spring.rabbitmq.listener;

import com.rabbitmq.client.GetResponse;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 主动拉取消息，进行消费 一次只拉取一条消息进行处理
 * 一个队列可以通过MQ主动推的方式和拉的方式一起消费。
 *
 * @auth 十三先生
 * @date 2023/12/15
 * @desc
 */
@Component
public class PullMessageListener {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public Object pullMessage(String queue) {

        return rabbitTemplate.execute(channel -> {
            boolean isContinue = Boolean.TRUE;
            int count = 0;
            while (isContinue) {
                //没有消息的时候 response 为空
                GetResponse response = channel.basicGet(queue, Boolean.FALSE);
                if (null == response || response.getMessageCount() < 0) {
                    isContinue = Boolean.FALSE;
                    continue;
                }
                doConsume(new String(response.getBody()));
                //TODO 对一些重要的业务场景要做好幂等。
                channel.basicAck(response.getEnvelope().getDeliveryTag(), Boolean.FALSE);
                count++;
            }
            return count;
        });
    }

    public void doConsume(String message) {
        System.out.println("pull" + message);
    }
}
