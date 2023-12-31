package com.spring.rabbitmq.listener;

import org.springframework.stereotype.Component;

/**
 * 创建队列时设置消息过期时间 声明队列添加属性：x-message-ttl 单位为 毫秒
 * 队列消息过期 处理类。
 *
 * @auth 十三先生
 * @date 2023/12/30
 * @desc
 */
@Component
public class TtlQueueMessageListener {
}
