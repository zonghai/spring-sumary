package com.spring.rabbitmq.config;

import com.spring.rabbitmq.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * 使用自定义配置的方式。去掉注解@Configuration 使用Spring-boot的默认方式。
 *
 * @auth 十三先生
 * @date 2023/12/19
 * @desc
 */
@Configuration
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
        //发送方的和消费方使用不同的connection 避免因为channel数量达到上限导致为，或MQ限流导致问题。
        rabbitTemplate.setUsePublisherConnection(Boolean.TRUE);
        //必须设置为true，否则消息消息路由失败也无法触发Return回调
        rabbitTemplate.setMandatory(Boolean.TRUE);
        //ack=false 需要自己去处理表示 producer发到exchange失败。
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("confirm callback,correlationData={},ack={},cause={}", correlationData == null ? "" : correlationData.getId(), ack, cause));
        //exchange-->queue消息投递失败会有returnsCallback，投递成功则不会有。
        rabbitTemplate.setReturnsCallback((message) -> log.info("returns callback,exchange={},routing={},message={}", message.getExchange(), message.getRoutingKey(), message.getReplyCode() + message.getReplyText()));
        return rabbitTemplate;
    }

    /**********************DirectExchange***************************/

    @Bean("directExchange")
    public Exchange directExchange() {
        Exchange exchange = new DirectExchange(Constants.EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        return exchange;
    }

    @Bean("directQueue")
    public Queue directQueue() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return new Queue(Constants.QUEUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, arguments);
    }

    @Bean("directBinding")
    public Binding directBinding(@Qualifier("directQueue") Queue directQueue, @Qualifier("directExchange") Exchange directExchange) {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return BindingBuilder.bind(directQueue).to(directExchange).with(Constants.ROUTING).and(arguments);
    }

    @Bean("pullExchange")
    public Exchange pullExchange() {
        Exchange exchange = new DirectExchange(Constants.PULL_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        return exchange;
    }

    @Bean("pullQueue")
    public Queue pullQueue() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return new Queue(Constants.PULL_QUEUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, arguments);
    }

    @Bean("pullBinding")
    public Binding pullBinding(@Qualifier("pullQueue") Queue directQueue, @Qualifier("pullExchange") Exchange directExchange) {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return BindingBuilder.bind(directQueue).to(directExchange).with(Constants.PULL_ROUTING).and(arguments);
    }

    /**********************TopicExchange***************************/
    @Bean("topicExchange")
    public TopicExchange topicExchange() {
        TopicExchange exchange = new TopicExchange(Constants.TOPIC_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        return exchange;
    }

    @Bean("topicQueue")
    public Queue topicQueue() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return new Queue(Constants.TOPIC_QUEUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, arguments);
    }

    @Bean("topicBinding")
    public Binding topicBinding(@Qualifier("topicQueue") Queue topicQueue, @Qualifier("topicExchange") TopicExchange topicExchange) {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return BindingBuilder.bind(topicQueue).to(topicExchange).with(Constants.TOPIC_ROUTING);
    }

    /**********************FanoutExchange***************************/
    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange() {
        FanoutExchange exchange = new FanoutExchange(Constants.FANOUT_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        return exchange;
    }

    @Bean("fanoutQueue")
    public Queue fanoutQueue() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return new Queue(Constants.FANOUT_QUEUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, arguments);
    }

    @Bean("fanoutBinding")
    public Binding fanoutBinding(@Qualifier("fanoutQueue") Queue fanoutQueue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }

    /**********************HeaderExchange***************************/
    @Bean("headerExchange")
    public HeadersExchange headerExchange() {
        HeadersExchange exchange = new HeadersExchange(Constants.HEADER_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        return exchange;
    }

    @Bean("headerQueue")
    public Queue headerQueue() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return new Queue(Constants.HEADER_QUEUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, arguments);
    }

    @Bean("headerBinding")
    Binding headerBinding() {
        return BindingBuilder.bind(headerQueue()).to(headerExchange())
                //如果将来消息头部中包含 name 属性，就算匹配成功
                .where("name").exists();
    }

    /**********************Delay Exchange 使用插件***************************/
    @Bean("delayExchange")
    public CustomExchange delayExchange() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("x-delayed-type", "direct");
        CustomExchange exchange = new CustomExchange(Constants.DELAY_MSG_EXCHANGE, "x-delayed-message", Boolean.TRUE, Boolean.FALSE,arguments);
        return exchange;
    }

    @Bean("delayQueue")
    public Queue delayQueue() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return new Queue(Constants.DELAY_MSG_QUEUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, arguments);
    }

    @Bean
    Binding delayBinding() {
        Map<String, Object> arguments = new HashMap();
        arguments.put("", "");
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(Constants.DELAY_MSG_ROUTING).and(arguments);
    }

}
