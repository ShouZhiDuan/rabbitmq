package com.rabbitmq.dsz.controller;

import com.rabbitmq.dsz.MsgDTO;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/14 18:43
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/send")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private SimpleMessageListenerContainer container;


    @GetMapping("/msg")
    public Object sendMsg(String queueName, String msg){
        log.info("发送队列：{}的消息：{}",queueName, msg);
        rabbitAdmin.declareQueue(new Queue(queueName));
        //rabbitTemplate.convertAndSend(queueName,msg);
        rabbitTemplate.convertAndSend(queueName,new MsgDTO("test_name", "浙江省杭州市滨江区"));
        log.info("消费队列：{}的消息：{}",queueName, msg);
        container.setQueueNames(queueName);
        return "OK";
    }


}
