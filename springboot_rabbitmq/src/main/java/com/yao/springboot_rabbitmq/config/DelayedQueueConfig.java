package com.yao.springboot_rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.config
 * @className: DelayedQueueConfig
 * @author: yao
 * @description: TODO
 * @date: 2022/4/21 20:25
 * @version: 1.0
 */
@Configuration
public class DelayedQueueConfig {
    //    队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //    交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //    routing_key
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }
    // 自定义交换机 我们在这里定义的是一个延迟交换机
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        // 自定义交换机的类型
        args.put("x-delayed-type", "direct");
        /***
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.其他配置
         * */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }
    // 绑定
    @Bean
    public Binding bindingDelayedQueue(
            @Qualifier("delayedQueue") Queue queue,
            @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(queue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
