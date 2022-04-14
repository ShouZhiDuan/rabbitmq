package com.rabbitmq.dsz.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: ShouZhi@Duan
 * @Description:
 */
@Configuration
public class RabbitMqConfig {
    @Bean
    public DirectExchange dszDirectExchange() {
        //durable 表示消息是否持久化、autoDelete 消息是否自动删除
        DirectExchange directExchange = new DirectExchange("DSZ_DIRECT_EXCHANGE", true, false);
        return directExchange;
    }
    @Bean
    public Queue dszQueue() {
        //exclusive:是否排外的  一般等于true的话用于一个队列只能有一个消费者来消费的场景
        Queue queue = new Queue("DSZ_DIRECT_QUEUE", true, false, false);
        return queue;
    }
    @Bean
    public Binding dszBinder() {
        return BindingBuilder.bind(dszQueue()).to(dszDirectExchange()).with("DSZ_DIRECT_QUEUE_KEY");
    }

    @Bean
    public TopicExchange dszTopicExchange() {
        TopicExchange topicExchange = new TopicExchange("TIAN_SHU_TOPIC_EXCHANGE", true, false);
        return topicExchange;
    }

    /**
     * Bean装载
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory defaultConnectionFactory){
        return new RabbitAdmin(defaultConnectionFactory);
    }

    @Autowired
    ConnectionFactory connectionFactory;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(HandleServer handleServer){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);       //初始化消费者
        container.setMaxConcurrentConsumers(10);   //最多消费者
        //container.setQueueNames("rabbitAdmin-key-3");  // 指定监听的队列s
        //container.setAcknowledgeMode(AcknowledgeMode.MANUAL);  //消费者主动确认
        container.setExposeListenerChannel(true);
        container.setMessageListener(handleServer);    //指定消息监听对象
        return container;
    }
}
