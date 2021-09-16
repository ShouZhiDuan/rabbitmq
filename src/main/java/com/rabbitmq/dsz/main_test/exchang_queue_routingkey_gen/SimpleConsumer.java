package com.rabbitmq.dsz.main_test.exchang_queue_routingkey_gen;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
public class SimpleConsumer {
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

    /**
     * 消费direct模式消息
     */
    public static void directConsumer(Channel channel) throws IOException, TimeoutException {
//        String EXCHANGE_TYPE = "direct";
//        String EXCHANGE_NAME = "";
//        String QUEUE_NAME = "direct-deafault-queue";
//        String ROUTING_KEY = "direct-deafault-queue";
//        //channel.exchangeDeclare(EXCHANGE_NAME,EXCHANGE_TYPE,true, false, null);
//        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//        //channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);

        String QUEUE_NAME = ExchangeQueueKeyGen.qName;
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("direct模式收到消息: " + msg );
                System.out.println("consumerTag : " + consumerTag );
                System.out.println("deliveryTag : " + envelope.getDeliveryTag() );
            }
        };
        // 开始获取消息
        channel.basicConsume(QUEUE_NAME, true, consumer);
        channel.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        /**
         * direct模式消费
         */
        directConsumer(channel);
    }



}
