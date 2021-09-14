package com.rabbitmq.dsz.main_test.test;

import com.rabbitmq.client.*;
import com.rabbitmq.dsz.main_test.MqConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 消息消费端
 */
public class ConsumerMQ {

    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";

    private final static String QUEUE_NAME = "test-queue-1";

    public static void main(String[] args) throws IOException, TimeoutException {
        consumer();
    }

    /**
     * 消息消费
     */
    public static void consumer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.10.33");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("dev_mq");
        connectionFactory.setPassword("123456");
        connectionFactory.setVirtualHost("/test");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // 声明交换机
        // String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true, false, null);
        // 声明队列
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 绑定队列和交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"gupao.best");
        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Received message : '" + msg + "'");
                System.out.println("consumerTag : " + consumerTag );
                System.out.println("deliveryTag : " + envelope.getDeliveryTag() );
            }
        };
        // 开始获取消息
        // String queue, boolean autoAck, Consumer callback
        channel.basicConsume(QUEUE_NAME, true, consumer);
        channel.close();
        connection.close();
    }
}
