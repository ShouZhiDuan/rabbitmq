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
        /**
         * 声明交换机
         * String exchange：交换机名称。
         * String type：交换机类型(direct、fanout、headers、topic)。
         * boolean durable： 重启是否保留当前交换机。
         * boolean autoDelete：是否自动删除。
         * Map<String, Object> arguments：Arguments控制台创建时候设置的一些参数。
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true, false, null);
        /**
         * 声明队列
         * String queue：队列名称。
         * boolean durable：重启服务是否保留当前队列。
         * boolean exclusive：是否排他性队列。排他性队列只能在声明它的 Connection 中使用（可以在同一个 Connection 的不同的 channel 中使用），连接断开时自动删除。
         * boolean autoDelete：是否自动删除。如果为 true，至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时，队列会自动删除。
         * Map<String, Object> arguments：队列的其他属性，例如：
         * Message TTL  | Auto expire  | Max length  | Max length bytes  | Overflow behaviour
         * Dead letter exchange  | Dead letter routing key  | Single active consumer  | Maximum priority
         * Lazy mode  Master locator
         */
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
