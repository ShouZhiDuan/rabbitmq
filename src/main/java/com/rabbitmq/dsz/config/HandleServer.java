package com.rabbitmq.dsz.config;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class HandleServer implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        String messageId = message.getMessageProperties().getMessageId();
        log.debug("HandleServer 消息ID为：{},消费DeliveryTag:{}",messageId,message.getMessageProperties().getDeliveryTag());
        try {
            int a = 1/0;
            //channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            log.debug("======消费异常再来一次======");
            throw e;
        } finally {

        }
    }

//    @Override
//    public void onMessage(Message message, Channel channel) throws IOException {
//        String consumerQueue = message.getMessageProperties().getConsumerQueue();
//        String msg = new String(message.getBody());
//        log.info("======再消费一次======");
//        log.info("HandleServer 消费者收到队列的线程ID:{}", Thread.currentThread().getName());
//        log.info("HandleServer 消费者收到队列:{}", consumerQueue);
//        log.info("HandleServer 消费者收到消息:{}", msg);
//        log.info("HandleServer 消费者收到消息ID:{}",  message.getMessageProperties().getDeliveryTag());
//        int a = 1/0;
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//        log.info("消费异常：" + message.getMessageProperties().getMessageId());
//    }


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
