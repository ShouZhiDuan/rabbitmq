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
     * direct、fanout、topic模式消费
     * 三者相同
     */
    public static void consumer(Channel channel) throws IOException, TimeoutException {
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
                System.out.println("收到消息: " + msg );
                System.out.println("consumerTag : " + consumerTag );
                System.out.println("deliveryTag : " + envelope.getDeliveryTag() );
            }
        };
        // 开始获取消息
        channel.basicConsume(QUEUE_NAME, true, consumer);
        channel.close();
    }

    /**
     * headers模式消费
     * 类似于交换机根据header的条件匹配动态分发消息给队列
     */
    public static void consumerHeaders(Channel channel) throws IOException, TimeoutException {
        /**
         * 首部交换机和扇形交换机一样不需要路由关键字，交换机时通过headers来将消息映射到队列的，
         * heders是一个hash结构求携带一个键“x-match”，
         * 这个键的value可以是any或者all，all代表消息携带的Hash是需要全部匹配，any代表仅匹配一个键就可以了。
         * 默认是all
         */
        String EXCHANGE_NAME = "header-exchane-test-6";
        // 声明一个headers交换机
        //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);
        // 声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();

        Map<String,Object> myHeaders = new HashMap();
        myHeaders.put("tag",1);
        myHeaders.put("x-match","any");
        // 将队列绑定到指定交换机上
        channel.queueBind(queueName, EXCHANGE_NAME, "", myHeaders);

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("收到消息: " + msg );
                System.out.println("consumerTag : " + consumerTag );
                System.out.println("deliveryTag : " + envelope.getDeliveryTag() );
            }
        };
        channel.basicConsume(queueName, true, consumer);
        //channel.close();
    }


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        /**direct、fanout、topic模式消费三者相同*/
        //consumer(channel);
        /**headers模式消费，与上面三种有点区别*/
        consumerHeaders(channel);
    }



}
