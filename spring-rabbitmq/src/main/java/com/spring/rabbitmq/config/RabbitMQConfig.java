package com.spring.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;


/**
 * 使用自定义配置的方式。去掉注解@Configuration 使用Spring-boot的默认方式。
 *
 * @auth 十三先生
 * @date 2023/12/19
 * @desc
 */
//@Configuration
public class RabbitMQConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Bean
    public CachingConnectionFactory cachingConnectionFactory(RabbitProperties properties) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setVirtualHost(properties.getVirtualHost());
        connectionFactory.setConnectionTimeout(Integer.valueOf(String.valueOf(properties.getConnectionTimeout().toSeconds())));

        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setChannelCheckoutTimeout(Integer.valueOf(String.valueOf(properties.getCache().getChannel().getCheckoutTimeout().toSecondsPart())));
        connectionFactory.setChannelCacheSize(properties.getCache().getChannel().getSize());
        //必须设置为true，否则消息消息路由失败也无法触发Return回调
        connectionFactory.setPublisherReturns(properties.isPublisherReturns());
        connectionFactory.setPublisherConfirmType(properties.getPublisherConfirmType());
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //必须设置为true，否则消息消息路由失败也无法触发Return回调
        rabbitTemplate.setMandatory(Boolean.TRUE);
        //ack=false 需要自己去处理表示 producer发到exchange失败。
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
                log.info("confirm callback,correlationData={},ack={},cause={}", correlationData.getId(), ack, cause)
        );
        //exchange-->queue消息投递失败会有returnsCallback，投递成功则不会有。
        rabbitTemplate.setReturnsCallback((message) ->
                log.info("returns callback,exchange={},routing={},message={}", message.getExchange(), message.getRoutingKey(), message.getReplyCode() + message.getReplyText())
        );
        return rabbitTemplate;
    }

}
