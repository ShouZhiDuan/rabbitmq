# rabbitmq
RabbitMQ知识学习

## 一、通用手动发送消费方式
* com.rabbitmq.dsz.main_test.test

## 二、添加一些关于交换机和队列声明描述
* com.rabbitmq.dsz.main_test.test.ConsumerMQ.consumer

## 三、交换机模式
### 1、direct直连模式，简单理解不需要交换机直接指定Queue,其实有个默认的Exchange。
* Exchange	(AMQP default) 默认交换机
* Routing Key	direct-queue-name2 队列名称为路由key
* 参考com.rabbitmq.dsz.main_test.direct
### 2、fanout广播，无需路由键
### 3、topic正则路由
### 4、headers头信息路由


## 四、账户权限相关操作
* rabbitmqctl add_user admin admin 
* rabbitmqctl set_user_tags admin administrator 
* rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"

## 五、RabbitMQ原生API操作交换机、队列、RoutingKey代码层面手动绑定操作。实现各种交换机的消息收发功能。
* com.rabbitmq.dsz.main_test.exchang_queue_routingkey_gen

## 六、Docker安装RabbitMQ并集成延时队列功能
* 参考：https://blog.csdn.net/u010375456/article/details/106323962/


## 学习参考
### Channel
* https://stackoverflow.com/questions/18418936/rabbitmq-and-relationship-%20between-channel-and-connection
### 官网
* https://www.rabbitmq.com/getstarted.html
### 常用操作
* https://www.cnblogs.com/vipstone/p/9295625.html















