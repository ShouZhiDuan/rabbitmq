package com.rabbitmq.dsz.main_test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/14 15:32
 * @Description:
 */
public class MainTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.10.33");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("dev_tianshu");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("tianshu");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        String msg = "Hello world, Rabbit MQ";
        channel.queueDeclare("skadi-1", false, false, false, null);
        channel.basicPublish("", "skadi-1", null, msg.getBytes());
        channel.close();
        connection.close();
        System.out.println("发送成功");
    }
}
