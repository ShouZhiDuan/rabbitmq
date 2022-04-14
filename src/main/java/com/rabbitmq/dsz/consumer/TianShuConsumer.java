package com.rabbitmq.dsz.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/14 15:14
 * @Description:
 */
@Slf4j
//@Component
public class TianShuConsumer {

    @RabbitListener(queues = {"skadi-1"})
    @RabbitHandler
    public void consumerMsg(Message message, Channel channel) throws IOException {
        log.info("消费消息:{}", message);
        //   //手工签收
        //   Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //   log.info("接受deliveryTag:{}", deliveryTag);
        //   channel.basicAck(deliveryTag, false);
    }

}