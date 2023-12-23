
**环境搭建**
    
    spring-boot 版本3.0.2
    jdk版本 17 
    中间件：RabbitMQ
    数据库：MySQL Redis
    配置中心：Nacos
    注册中心：Nacos
    Spring-boot-admin

**RabbitMQ**

**修改消费者并发数**

    动态修改消费者并发数-UpdateMQConcurrencyController
    地址：https://blog.csdn.net/u012988901/article/details/126971334
    controller目录 
        UpdateMQConcurrencyController 
        以http的方式动态修改RabbitMQ的最小和最大并发消费者数量
    
    Listener 使用注解声明队列，exchange routing 并绑定，设置固定并发消费者数量（最大or最小并发消费者数量）
    @RabbitListener 注解支持spring配置，取值方式 ${} 
        concurrency属性 
            单个值为整形时是最小的并发数，若配置了最大并发数 maxConcurrentConsumers 这个数至少是最小并发数
            可以通过字符串配置最大最小并发数 规则 数字-数字

**单活消费者**
    
    表示队列中可以注册多个消费者，但是只允许一个消费者消费消息，只有在此消费者出现异常时，才会自动转移到另一个消费者进行消费。
    单一活跃消费者适用于需要保证消息消费顺序性，同时提供高可靠能力的场景
    使用注解的方式如下
    arguments = {@Argument(name = "x-single-active-consumer",value = "true",type = "java.lang.Boolean")}
    代码方式

**消费方式-Pull**

    使用场景：有些情况下推模式并不适用的，由于某些限制，消费者在某个条件成立时才能消费消息
    一次只拉取一条消息进行消费。
    参考类：PullMessageController PullMessageListener两个类

**消费方式-Push**

    参考类 MessageListener
    写代码时，最好对ack和nack方法单独定义，进行trycatch。
    对于一些重要的业务场景，注意幂等。

消息确认机制
    
    消费者ack(acknowledge，确认or回执)机制
        消费者处理完消息后 进行确认，或处理异常进行nack ；确认方式： auto-自动确认 mq Push消息后进行ack ，manual-手动确认
    生产者确认机制
        消息的可靠性投递 publisher-confirms: true
        ConfirmCallback 表示生产者发消息到 exchange 后发送的消息，成功失败都会有消息。处理方式 ConfirmCallback 回调方式。
        ReturnCallback  表示生产者发消息到 queue，解决消息无法路由到指定队列的问题 只有失败有回调。要注意的是，ReturnCallback 机制只有在消息被发送到交换机后，才会触发
        当exchange无法找到任何一个合适的queue时，将消息return给生产者
        spring.rabbitmq.template.mandatory=true 必须设置为true，否则消息消息路由失败也无法触发Return回调 spring.rabbitmq.publisher-returns=true
**事务消息**

    spring.rabbitmq.publisher-confirms一定要配置为false，否则会与事务处理相冲突，启动时会报异常
    rabbitTemplate.setChannelTransacted(Boolean.TRUE); 
    并且新建事务管理器 RabbitTransactionManager
    再发送消息的方法加注解 @Transactional(rollbackFor = Exception.class,transactionManager = "rabbitTransactionManager")

**常用命令**

**设置内存大小**
    
    按照内存大小
    /Users/zonghai/tools/rabbitmq_server-3.8.16/sbin/rabbitmqctl set_vm_memory_high_watermark absolute "1G"
    按照百分比。
    rabbitmqctl set_vm_memory_high_watermark 0.6

**查询和设置超时时间**
    
    /Users/zonghai/tools/rabbitmq_server-3.8.16/sbin/rabbitmqctl eval 'application:get_env(rabbit,consumer_timeout).'
    /Users/zonghai/tools/rabbitmq_server-3.8.16/sbin/rabbitmqctl eval 'application:set_env(rabbit,consumer_timeout,180000000).'
    注意：这个是临时更改，永久更改需要进入rabbitmq.conf文件里修改，修改consumer_timeout参

**修改用户名密码**
    
    命令行方式修复用户密码
    /Users/zonghai/tools/rabbitmq_server-3.8.16/sbin/rabbitmqctl change_password admin newpassword

**Spring RabbitMQ属性配置说明**
    
    https://www.bilibili.com/read/cv12580605/