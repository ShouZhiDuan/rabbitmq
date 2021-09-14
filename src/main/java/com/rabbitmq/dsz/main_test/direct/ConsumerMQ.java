package com.rabbitmq.dsz.main_test.direct;

import com.rabbitmq.client.*;
import com.rabbitmq.dsz.main_test.MqConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 消息消费端
 */
public class ConsumerMQ {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectionUtil.getConnection("192.168.10.33", 5672, "root", "123456", "/");
        //direct持续消费
        //directContinueConsumer(connection);
        //单个消费
        directOneConsumer(connection);
    }

    /**
     * direct连续消费模式
     */
    public static void directContinueConsumer(Connection conn) throws IOException {
        String queueName = "direct-queue-name";
        Channel channel = conn.createChannel();
        //参数一：队列名称
        //参数二：是否持久化
        //参数三：是否独占模式
        //参数四：消费者断开连接时是否删除队列
        //参数五：消息其他参数
        channel.queueDeclare(queueName, false, false, false, null);
        Consumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "utf-8"); // 消息正文
                System.out.println("收到消息 => " + message);
                // 手动确认消息
                // 参数一：该消息的index
                // 参数二：是否批量应答，true批量确认小于当前id的消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(queueName, false, "", defaultConsumer);
    }


    /**
     * direct单个消费模式
     */
    public static void directOneConsumer(Connection conn) throws IOException {
        String queueName = "direct-queue-name2";
        Channel channel = conn.createChannel();
        channel.queueDeclare(queueName, false, false, false, null);
        GetResponse resp = channel.basicGet(queueName, false);
        String message = new String(resp.getBody(), "UTF-8");
        System.out.println("消费到的消息：" + message);
        channel.basicAck(resp.getEnvelope().getDeliveryTag(), false); // 消息确认
    }













}
