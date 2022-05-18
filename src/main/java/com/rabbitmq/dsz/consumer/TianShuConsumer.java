package com.rabbitmq.dsz.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
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

    //@RabbitListener(queues = {"ts-connection-10123","ts-connection-10126"})
    @RabbitListener(queues = {"topic_shouzhi_queue"})
    @RabbitHandler
    public void consumerMsg(Message message, Channel channel) throws IOException {
        log.info("消费消息:{}", message);
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        try {
            int i = 1/0;
            log.info("接受deliveryTag:{}", deliveryTag);
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            log.info("消费异常deliveryTag:{}", deliveryTag);
            throw e;
        }finally {
            log.info("======重试消费deliveryTag:{}结束======", deliveryTag);
        }
    }

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "${topic_shouzhi_queue}",autoDelete = "false"),
//            exchange = @Exchange(value = "${mq.q}",type = "${mq.queueBinding.exchange.type}"),
//            key = "${mq.queueBinding.key}"
//    ))
//    public void infoConsumption(String data, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
//        log.info("RabbitReceiver.infoConsumption() data = {}",data);
//        int i = 10 /0;
//        log.error("支付异常，重新将消息放入mq");
//        //System.out.println(count++);
//    }

}