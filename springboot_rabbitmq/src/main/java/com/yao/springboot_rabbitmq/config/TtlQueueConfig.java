package com.yao.springboot_rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.config
 * @className: TtlQueueConfig
 * @author: yao
 * @description: TODO
 * @date: 2022/4/21 14:59
 * @version: 1.0
 */
@Configuration
public class TtlQueueConfig {

    //    普通交换机名称
    public static final String X_EXCHANGE = "X";

    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";

    public static final String Queue_a = "QA";

    public static final String Queue_b = "QB";

    public static final String Queue_c = "QC";

    public static final String DEAD_LETTER_QUEUE = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key", "YD");
        map.put("x-message-ttl", 10000);
        return QueueBuilder.durable(Queue_a).withArguments(map).build();
    }

    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key", "YD");
        map.put("x-message-ttl", 40000);
        return QueueBuilder.durable(Queue_b).withArguments(map).build();
    }

    @Bean("queueC")
    public Queue queueC() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(Queue_c).withArguments(map).build();
    }

    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding queueBBindX(
            @Qualifier("queueB") Queue queueB, @Qualifier("xExchange") DirectExchange xExchange) {

        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    @Bean
    public Binding queueABindX(
            @Qualifier("queueA") Queue queueA, @Qualifier("xExchange") DirectExchange xExchange) {

        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    @Bean
    public Binding queueCBindX(
            @Qualifier("queueC") Queue queueC, @Qualifier("xExchange") DirectExchange xExchange) {

        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }

    @Bean
    public Binding queueDBindY(
            @Qualifier("queueD") Queue queueD, @Qualifier("yExchange") DirectExchange yExchange) {

        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

    //

}
