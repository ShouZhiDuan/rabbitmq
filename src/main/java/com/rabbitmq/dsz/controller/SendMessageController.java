package com.rabbitmq.dsz.controller;

import com.rabbitmq.dsz.config.HandleServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/14 18:43
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/msg")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private SimpleMessageListenerContainer container;

    @Autowired
    private HandleServer handleServer;


//    @GetMapping("/msg")
//    public Object sendMsg(String queueName, String msg){
//        log.info("发送队列：{}的消息：{}",queueName, msg);
//        rabbitAdmin.declareQueue(new Queue(queueName));
//        //rabbitTemplate.convertAndSend(queueName,msg);
//        rabbitTemplate.convertAndSend(queueName,new MsgDTO("test_name", "浙江省杭州市滨江区"));
//        log.info("消费队列：{}的消息：{}",queueName, msg);
//        container.setQueueNames(queueName);
//        container.setMessageListener(handleServer);
//        return "OK";
//    }

    /**
     * http://localhost/msg/send?queueName=topic3
     */
    @GetMapping("/send")
    public Object sendMsg(String queueName){
        Map map = new HashMap();
        Map map2 = new HashMap();
        map.put("medusaIp","192.168.10.10");
        map.put("medusaPort","8080");
        map.put("projectId","1");
        map.put("method","test_method");
        map2.put("name","name");
        map2.put("categorical","test");
        map2.put("description","dec");
        map.put("features", Arrays.asList(map2));
        Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
        if(Objects.isNull(queueProperties)){
            rabbitAdmin.declareQueue(new Queue(queueName));
        }
        rabbitTemplate.convertAndSend(queueName,map);
        return "發送OK";
    }

    /**
     * http://localhost/msg/monitor?queueName=topic6
     */
    @GetMapping("/monitor")
    public Object monitorMsg(String queueName){
        Properties queueProperties = rabbitAdmin.getQueueProperties(queueName);
        if(Objects.isNull(queueProperties)){
            //為空創建
            rabbitAdmin.declareQueue(new Queue(queueName));
        }
        container.setQueueNames(queueName);
        container.setMessageListener(handleServer);
        return "監聽OK";
    }



}
