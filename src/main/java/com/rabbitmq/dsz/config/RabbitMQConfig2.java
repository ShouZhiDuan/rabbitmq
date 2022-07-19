package com.rabbitmq.dsz.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.NoBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.UUID;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/30 11:55
 * @Description:
 */
@Slf4j
@Configuration
public class RabbitMQConfig2 {

    @Autowired
    private HandleServer handleServer;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * 这种方式application.yml配置文件相关策略不会生效。
     */
//    @Bean
//    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory){
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//        container.setConcurrentConsumers(1);//<<配置文件中不会生效>>
//        container.setMaxConcurrentConsumers(10);//<<配置文件中不会生效>>
//        container.setExposeListenerChannel(true);//<<配置文件中不会生效>>
//        container.setPrefetchCount(1); //<<配置文件中不会生效>>
//        container.setAcknowledgeMode(AcknowledgeMode.AUTO); //<<配置文件中不会生效>>
//        container.setRetryDeclarationInterval(3000);//<<配置文件中不会生效>>
//        container.setDeclarationRetries(3);//<<配置文件中不会生效>>
//        container.setStatefulRetryFatalWithNullMessageId(true);
//        //container.setQueueNames("rabbitAdmin-key-3");
//        //container.setMessageListener(handleServer);
//        return container;
//    }


    /**
     * to see https://www.codeleading.com/article/39691078081/
     * 这种方式解决simple监听模式，生效配置文件规则。上面50行配置的方式无法生效application.yml关于RabbitMQ配置，比如说重试机制在配置文件simple不会生效上面的方式
     * 但是这种方式：spring.rabbitmq.listener.simple.retry.max-attempts=3会生效。
     */
    @Bean
    public SimpleMessageListenerContainer SimpleMessageListenerContainer(SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory) {
        SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
        endpoint.setMessageListener(handleServer);
        endpoint.setId(String.valueOf(UUID.randomUUID()));
        SimpleMessageListenerContainer container = simpleRabbitListenerContainerFactory.createListenerContainer(endpoint);
        // 配置队列信息
        //container.setQueueNames("队列1","队列2");
        // 配置重试
        container.setAdviceChain(createRetry());
        return container;
    }

    private RetryOperationsInterceptor createRetry() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.registerListener(new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                log.debug("第一次重试调用");
                return false;
            }
            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.debug("最后一次重试调用");
            }
            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.debug("每次重试调用");
            }
        });
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(5));
        retryTemplate.setBackOffPolicy(new NoBackOffPolicy());
        return RetryInterceptorBuilder.stateless()
                .retryOperations(retryTemplate)
                //.recoverer(new DefaultMessageRecoverer())
                .build();
    }

}
