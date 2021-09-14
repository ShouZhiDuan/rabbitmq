# rabbitmq
RabbitMQ知识学习

## 交换机模式
### direct直连模式，简单理解不需要交换机直接指定Queue,其实有个默认的Exchange。
* Exchange	(AMQP default) 默认交换机
* Routing Key	direct-queue-name2 队列名称为路由key
* 参考com.rabbitmq.dsz.main_test.direct
