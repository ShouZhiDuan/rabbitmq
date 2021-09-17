package com.rabbitmq.dsz.main_test.exchang_queue_routingkey_gen;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class SimpleProducer {

    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.10.33");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("dev_mq");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("/test");
        Connection connection = connectionFactory.newConnection();
        return connection;
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        /**
         * 1、发送direct默认的模式交换机到队列
         * 这种情况只要申明队列，然后RoutingKey和当前声明的队列名称一致就会走默认的direct交换机。
         */
        //以下为direct默认模式
//        channel.queueDeclare("direct-deafault-queue",true,false,false,null);
//        channel.basicPublish("","direct-deafault-queue",null,"666666-1".getBytes());
        //非默认模式
        //channel.basicPublish(ExchangeQueueKeyGen.exName,ExchangeQueueKeyGen.routingKey,null,"这是一条direct消息,key=direct.key.6".getBytes());


        /**
         * 2、fanout模式发送
         * 发现fanout模式可以不用指定routingKey,如果指定了routingKey也没有效果。
         * 消息默认都会被分发到所有fanout下的所有queue.
         */
        //channel.basicPublish(ExchangeQueueKeyGen.exName,ExchangeQueueKeyGen.routingKey,null,(System.currentTimeMillis()+"这是一条fanout消息").getBytes());
        /**
         * 3、topic模式发送
         * 正则路由RoutingKey
         *   abc.*  abc.#
         *   *表示匹配单个单词例如：abc.* -> abc.1 abc.q12
         *   #表示匹配所有内容例如：abc.# -> abc.asd.12 abc.qwe123
         */
        //topic.#           topic-ex-queue-6-3
        //topic.create.*    topic-ex-queue-6-5
        //order.*.tag   topic-ex-queue-66
        //channel.basicPublish(ExchangeQueueKeyGen.exName,"order.abc.tag",null,(System.currentTimeMillis()+"这是一条topic消息").getBytes());
        /**
         * 4、headers模式发送
         * 参考：https://blog.csdn.net/hry2015/article/details/79188615
         * 注意点：
         *  - headers模式必须要先启动消费者，否则在消费者启动之前的消息将会丢失，不会被消费之消费。
         *  - headers模式使用原生API时候监听状态期间不允许关闭channel。channel.close()否则监听不到消息。
         */
         sendHeaders(channel);
    }

    /**
     * 发送header类型的消息
     */
    public static void sendHeaders(Channel channel) throws IOException {
        String EXCHANGE_NAME = "header-exchane-test-6";
        Map<String,Object> headers = new HashMap<>();
        headers.put("tag",1);
        // 声明一个headers交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);
        String message = "我是headers消息39：" + System.currentTimeMillis();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .headers(headers)
                .build();
        channel.basicPublish(EXCHANGE_NAME, "", props, message.getBytes("UTF-8"));
        System.out.println(" [HeaderSend] Sent '" + headers + "':'" + message + "'");
    }


}
