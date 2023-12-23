package com.spring.rabbitmq.controller;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 动态修改RabbitMQ的最小和最大并发消费者数量
 *
 * @auth 十三先生
 * @date 2023/11/12
 * @desc
 */
@RestController
public class UpdateMQConcurrencyController {

    @Resource
    private RabbitListenerEndpointRegistry registry;

    @GetMapping("update")
    public String update(@RequestParam(value = "queueName") String queueName, @RequestParam(value = "min") Integer min, @RequestParam(value = "max") Integer max) {
        Collection<MessageListenerContainer> containers = registry.getListenerContainers();
        for (MessageListenerContainer container : containers) {
            SimpleMessageListenerContainer simple = (SimpleMessageListenerContainer) container;
            List<String> queueNames = Arrays.asList(simple.getQueueNames());
            if (queueNames.contains(queueName)) {
                simple.setMaxConcurrentConsumers(max);
                simple.setConcurrentConsumers(min);
            }
        }

        return "I'm alive.";
    }
}
