package com.rabbitmq.dsz.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.dsz.MsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/14 18:31
 * @Description:
 */
@Slf4j
@Service
public class HandleServer implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) {
        String consumerQueue = message.getMessageProperties().getConsumerQueue();
        String msg = new String(message.getBody());
        log.info("HandleServer 消费者收到队列:{}", consumerQueue);
        log.info("HandleServer 消费者收到消息:{}", msg);
    }

//    @Override
//    public void onMessage(Message message, Channel channel) {
//        log.info("HandleServer 消费者收到消息:{}", (new String(message.getBody())));
//        if(sendSuccess){
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//        }else if (message.getMessageProperties().getRedelivered()){
//            log.error("消息已重复处理失败,拒绝再次接收");
//            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
//        }else{
//            log.error("消息即将再次返回队列处理");
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        }
//    }
}
