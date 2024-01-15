**环境搭建**

    spring-boot 版本3.0.2
    jdk版本 17 
    中间件：RabbitMQ
    数据库：MySQL Redis
    配置中心：Nacos
    注册中心：Nacos
    Spring-boot-admin

Redis 常用命令
    
    strlen 字符串长度
    memoryusage 字符串占用内存
    批量删除模糊查询的字符串
    /Users/zonghai/tools/redis-7.0.0/bin/redis-cli -p 6379 keys "xx*" | xargs /Users/zonghai/tools/redis-7.0.0/bin/redis-cli -p 6379 del

