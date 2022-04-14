package com.rabbitmq.dsz;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Test
    void sendMsgTest() {
        rabbitTemplate.convertAndSend("key666666","key666666");
        //rabbitTemplate.convertAndSend("exchang1","key1","666666");
        //rabbitTemplate.convertAndSend("DSZ_DIRECT_EXCHANGE","DSZ_DIRECT_QUEUE_KEY","666666");
        //rabbitTemplate.convertAndSend("TIAN_SHU_TOPIC_EXCHANGE","partId-1","skadi-1-msg");
//        rabbitTemplate.setDefaultReceiveQueue("partId-2");
//        rabbitTemplate.convertAndSend("partId-2","skadi-1-msg");
    }

    @Test
    public void testMessage01() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述。。。");
        messageProperties.getHeaders().put("type", "自定义消息类型。。。");
        Message message = new Message("Hello RabbitMQ!".getBytes(), messageProperties);
        rabbitTemplate.convertAndSend("topic001", "skadi666666", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("----添加额外的设置----");
                message.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
                message.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
                return message;
            }
        });
    }

    @Test
    void testMessage(){
        rabbitAdmin.declareQueue(new Queue("rabbitAdmin-key-3"));
        rabbitTemplate.convertAndSend("rabbitAdmin-key-3","skadi-2-msg");
    }

}
