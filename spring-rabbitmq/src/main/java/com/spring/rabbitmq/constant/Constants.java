package com.spring.rabbitmq.constant;

/**
 * @auth 十三先生
 * @date 2023/12/24
 * @desc
 */
public class Constants {


    /**
     * direct exchange
     **/
    public static final String QUEUE = "direct.test";
    public static final String EXCHANGE = "direct.test";
    public static final String ROUTING = "direct.test";

    /**
     * topic exchange
     **/
    public static final String TOPIC_QUEUE = "topic.test";
    public static final String TOPIC_EXCHANGE = "topic.test";
    public static final String TOPIC_ROUTING = "topic.test.*";

    /**
     * fanout exchange
     **/
    public static final String FANOUT_QUEUE = "fanout.test";
    public static final String FANOUT_EXCHANGE = "fanout.test";
    /**
     * header exchange
     **/
    public static final String HEADER_QUEUE = "header.test";
    public static final String HEADER_EXCHANGE = "header.test";


    /**
     * single consumer
     **/
    public static final String SINGLE_QUEUE = "single.test";
    public static final String SINGLE_EXCHANGE = "single.test";
    public static final String SINGLE_ROUTING = "single.test";

    /**
     * single consumer
     **/
    public static final String PULL_QUEUE = "pull.test";
    public static final String PULL_EXCHANGE = "pull.test";
    public static final String PULL_ROUTING = "pull.test";


    /**
     * ttl  发消息的时候 设置消息的过期时间
     **/
    public static final String TTL_MSG_QUEUE = "ttl.msg.test";
    public static final String TTL_MSG_EXCHANGE = "ttl.msg.test";
    public static final String TTL_MSG_ROUTING = "ttl.msg.test";


    /**
     * 延时消息
     **/
    public static final String DELAY_MSG_QUEUE = "delay.msg.test";
    public static final String DELAY_MSG_EXCHANGE = "delay.msg.test";
    public static final String DELAY_MSG_ROUTING = "delay.msg.test";


}
