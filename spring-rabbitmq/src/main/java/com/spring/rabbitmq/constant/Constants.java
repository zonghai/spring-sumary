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
    public static final String QUEUE = "direct.test.queue";
    public static final String EXCHANGE = "direct.test.exchange";
    public static final String ROUTING = "direct.test.routing";

    /**
     * topic exchange
     **/
    public static final String TOPIC_QUEUE = "topic.test.queue";
    public static final String TOPIC_EXCHANGE = "topic.test.exchange";
    public static final String TOPIC_ROUTING = "topic.test.*";

    /**
     * fanout exchange
     **/
    public static final String FANOUT_QUEUE = "fanout.test.queue";
    public static final String FANOUT_EXCHANGE = "fanout.test.exchange";
    /**
     * header exchange
     **/
    public static final String HEADER_QUEUE = "header.test.queue";
    public static final String HEADER_EXCHANGE = "header.test.exchange";


    /**
     * single consumer
     **/
    public static final String SINGLE_QUEUE = "single.test.queue";
    public static final String SINGLE_EXCHANGE = "single.test.exchange";
    public static final String SINGLE_ROUTING = "single.test.routing";

    /**
     * single consumer
     **/
    public static final String PULL_QUEUE = "pull.test.queue";
    public static final String PULL_EXCHANGE = "pull.test.exchange";
    public static final String PULL_ROUTING = "pull.test.routing";


    /**
     * ttl  发消息的时候 设置消息的过期时间
     **/
    public static final String TTL_MSG_QUEUE = "ttl.test.queue";
    public static final String TTL_MSG_EXCHANGE = "ttl.test.exchange";
    public static final String TTL_MSG_ROUTING = "ttl.test.routing";


    /**
     * 延时消息
     **/
    public static final String DELAY_MSG_QUEUE = "delay.test.queue";
    public static final String DELAY_MSG_EXCHANGE = "delay.test.exchange";
    public static final String DELAY_MSG_ROUTING = "delay.test.routing";


    /**
     * 队列类型  quorum队列
     **/
    public static final String QUORUM_MSG_QUEUE = "quorum.test.queue";
    public static final String QUORUM_MSG_EXCHANGE = "quorum.test.exchange";
    public static final String QUORUM_MSG_ROUTING = "quorum.test.routing";
    /**
     * 队列类型  惰性队列
     **/
    public static final String LAZY_MSG_QUEUE = "lazy.test.queue";
    public static final String LAZY_MSG_EXCHANGE = "lazy.test.exchange";
    public static final String LAZY_MSG_ROUTING = "lazy.test.routing";

    /**
     * 队列类型  惰性队列
     **/
    public static final String TRANSACTION_QUEUE = "transaction.test.queue";
    public static final String TRANSACTION_EXCHANGE = "transaction.test.exchange";
    public static final String TRANSACTION_ROUTING = "transaction.test.routing";



}
