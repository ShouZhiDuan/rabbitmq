package com.rabbitmq.dsz.main_test.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.dsz.main_test.MqConnectionUtil;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: ShouZhi@Duan
 * @Description: 消息发送端
 */
public class ProducerMQ {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = MqConnectionUtil.getConnection("192.168.10.33", 5672, "root", "123456", "/");
        //direct发送模式
        directProducer(connection);
    }

    /**
     * direct直连->发送模式
     * 默认交换机为空
     * 但是需要制定queue名称
     */
    public static void directProducer(Connection connection) throws IOException {
        String exchangeName = "my-direct-exchange";//direct模式支持默认空""的交换机
        String directQueueName = "direct-queue-name6";
        Channel channel = connection.createChannel();
        //参数一：队列名称
        //参数二：是否持久化
        //参数三：是否独占模式
        //参数四：消费者断开连接时是否删除队列
        //参数五：消息其他参数
        channel.queueDeclare(directQueueName, false, false, false, null);
        String message = String.format(directQueueName + "当前时间：%s", new Date().getTime());
        //参数一：交换机名称。
        //参数二：队列名称。
        //参数三：消息的其他属性-路由的headers信息。
        //参数四：消息主体
        channel.basicPublish(exchangeName, directQueueName, null, message.getBytes("UTF-8"));
    }






}
