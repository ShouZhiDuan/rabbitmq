package com.rabbitmq.dsz.main_test.exchang_queue_routingkey_gen;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
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
        channel.basicPublish(ExchangeQueueKeyGen.exName,ExchangeQueueKeyGen.routingKey,null,"这是一条direct消息,key=direct.key.6".getBytes());


        /**
         * 2、fanout模式发送
         * 发现fanout模式可以不用指定routingKey,如果指定了routingKey也没有效果。
         * 消息默认都会被分发到所有fanout下的所有queue.
         */
        //channel.basicPublish("fanout-ex-6","fanout.key",null,"第二次发一次带routingKey的fanout消息".getBytes());
        /**
         * 3、topic模式发送
         * 正则路由RoutingKey
         *   abc.*  abc.#
         *   *表示匹配单个单词例如：abc.* -> abc.1 abc.q12
         *   #表示匹配所有内容例如：abc.# -> abc.asd.12 abc.qwe123
         */
        //topic.#           topic-ex-queue-6-3
        //topic.create.*    topic-ex-queue-6-5
        //channel.basicPublish("topic-ex-6","topic.abc",null,"发送一条key=topic.abc的消息".getBytes());
        /**
         * 4、headers模式发送
         * 参考：https://blog.csdn.net/hry2015/article/details/79188615
         */
//        HashMap headers = new HashMap();
//        headers.put("format","pdf");
//        headers.put("x-match","all");
//        //headers.put("x-match","any");
//        AMQP.BasicProperties properties = new AMQP.BasicProperties();
//        properties.builder().headers(headers).build();
//        channel.basicPublish("topic-ex-6","topic.abc",properties,"发送一条带header的消息".getBytes());

    }


}
