# rabbitmq
RabbitMQ知识学习

## 通用手动发送消费方式
* com.rabbitmq.dsz.main_test.test

## 添加一些关于交换机和队列声明描述
* com.rabbitmq.dsz.main_test.test.ConsumerMQ.consumer

## 交换机模式
### direct直连模式，简单理解不需要交换机直接指定Queue,其实有个默认的Exchange。
* Exchange	(AMQP default) 默认交换机
* Routing Key	direct-queue-name2 队列名称为路由key
* 参考com.rabbitmq.dsz.main_test.direct


## 学习参考
### Channel
* https://stackoverflow.com/questions/18418936/rabbitmq-and-relationship-%20between-channel-and-connection
### 官网
* https://www.rabbitmq.com/getstarted.html
### 常用操作
* https://www.cnblogs.com/vipstone/p/9295625.html