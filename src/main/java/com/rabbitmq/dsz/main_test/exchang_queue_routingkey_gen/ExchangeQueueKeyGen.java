package com.rabbitmq.dsz.main_test.exchang_queue_routingkey_gen;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 交换机、队列、路由key申明以及绑定操作
 */
public class ExchangeQueueKeyGen {

    /**
     * direct、fanout、headers、topic
     * 其中fanout广播模式可以不用指定routingKey
     */
    private static String exType = "headers";
    private static String exName = "headers-ex-6";
    private static String qName = "headers-ex-queue-6-1";
    private static String routingKey = "";

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
        //获取连接
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exName,exType,true, false, null);// 声明交换机
        channel.queueDeclare(qName, true, false, false, null);// 声明队列
        channel.queueBind(qName,exName,routingKey);// 绑定队列和交换机
        channel.close();
        connection.close();
    }

}
