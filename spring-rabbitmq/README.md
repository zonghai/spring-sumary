
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
    单个消费者，可以解决顺序消费的问题（相比RocketMQ，并发性比较差。）

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

**exchange**

    DirectExchange 精准匹配
    TopicExchange  模糊匹配
    FanoutExchange 广播 发消息到与之绑定的队列，与RocketMQ的广播不是一个意思；发消息时routing为空即可。
    HeaderExchange 不常用。

**消息过期**
    
    过期时间TTL（Time To Live）
    过期时间TTL表示可以对消息设置预期的时间，在这个时间内都可以被消费者接收获取；过了之后消息将自动被删除。
    目前有两种方法可以设置
        第一种方法是通过队列属性设置，队列中所有消息都有相同的TTL。
        第二种方法是对消息进行单独设置，每条消息TTL可以不同。
        当同时指定了 queue 和 message 的 TTL 值，则两者中较小的那个才会起作用。
    延时队列
        通过插件实现
        插件下载地址：https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases
        安装命令 /Users/zonghai/tools/rabbitmq_server-3.8.16/sbin/rabbitmq-plugins enable rabbitmq_delayed_message_exchange
        执行完成，重启RabbitMQ 即安装完成。
        发消息时设置：message.getMessageProperties().setHeader("x-delay", "过期时间，单位毫秒")；本人测试时，配置returns callback 会有这个提示 "returns callback,exchange=delay.msg.test,routing=delay.msg.test,message=312NO_ROUTE"
        原理：
            RabbitMq通过使用rabbitmq_delayed_message_exchange插件实现延迟消息。
            它与TTL方式不同的是，TTL方式存放在死信队列(dalayqueue) 里，它是基于插件存放消息放在延时交换机里面（x-delayed-message exchange） 
            生产者将消息(msg)和路由键(routekey)发送指定的延时交换机(exchange)上
            延时交换机(exchange)存储消息等待消息到期根据路由键(routekey)找到绑定自己的队列 (queue)并把消息给它
            队列(queue)再把消息发送给监听它的消费者(customer）
**队列类型**  
    
    普通队列-classic
    仲裁队列-quorum (给队列设置参数 x-queue-type 值为 quorum 就可将队列变为仲裁队列)
    普通队列和仲裁队列的区别在于：
    普通队列只会存放在集群中的一个节点上，虽然通过其它节点访问普通队列，但是其它节点只是把请求转发到队列所在的节点进行操作。一旦队列所在节点如果宕机，队列中的消息就会丢失，因此普通集群只是提高了并发能力，并未实现高可用。
    仲裁队列是 3.8 版本以后才有的新功能，用来替代镜像队列，属于主从模式，支持基于 Raft 协议强一致的主从数据同步。虽然请求仍然都是由主节点进行操作，然后同步到从节点中。但是对于任何节点来说，既可能是某个仲裁队列的主节点，也可能是其它仲裁队列的从节点。
    因此也具有分散节点压力，提高并发访问的特点。另外如果主节点挂了，其中的某个从节点就会变成主节点，并在其它节点上尽可能创建出新的主节点，保障主从数量一致。一个仲裁队列的默认数量是 5，即一个主节点，4个副本节点，如果集群中节点数量少于 5 ，比如我们搭建了 3 个节点的集群，
    那么创建的仲裁队列就是 1 主 2 副本。当然如果集群中的节点数大于 5 个的话，那么就只会在 5 个节点中创建出 1 主 4 副本。由此可见：仲裁队列使用非常简单，集群中使用仲裁队列，可以极大的保障 RabbitMQ 集群对接的高可用。

**惰性队列**

    基于磁盘存储，消息上限高 
    没有间歇性的page-out，性能比较稳定
    基于磁盘存储，消息时效性会降低
    性能受限于磁盘的IO




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