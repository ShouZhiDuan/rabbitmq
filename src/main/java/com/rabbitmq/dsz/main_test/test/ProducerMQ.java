package com.rabbitmq.dsz.main_test.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 消息发送端
 */
public class ProducerMQ {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";

    private final static String QUEUE_NAME = "test-queue-1";

    /**
     * 消息发送
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        String exName = "direct-ex-1";
        String qName = "testQueue-1";
        String exType = "direct";
        String routingKey = "gupao.best.1";
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.10.33");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("dev_mq");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("/test");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        /**
         * 声明交换机、队列、RoutingKey绑定关系
         */
        channel.exchangeDeclare(exName,exType,true, false, null);// 声明交换机
        channel.queueDeclare(qName, true, false, false, null);// 声明队列
        channel.queueBind(qName,exName,routingKey);// 绑定队列和交换机
        /**
         * 发送消息
         */
        String msg = "Hello world, Rabbit MQ";
        channel.basicPublish(exName, routingKey, null, msg.getBytes());
        channel.close();
        connection.close();
    }
}
