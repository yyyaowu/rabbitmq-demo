package com.yao.springboot_rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.config
 * @className: ConfirmConfig
 * @author: yao
 * @description: TODO 发布确认高级
 * @date: 2022/4/21 20:39
 * @version: 1.0
 */
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";

    public static final String BACKUP_QUEUE_NAME = "backup.queue";

    public static final String WARNING_QUEUE_NAME = "warning.queue";
    // 声明业务Exchange
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {

        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }
    // 声明确认队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    // 声明确认队列绑定关系
    @Bean
    public Binding queueBinding(
            @Qualifier("confirmQueue") Queue queue,
            @Qualifier("confirmExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }

    // 备份交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(
            @Qualifier("backupQueue") Queue queue,
            @Qualifier("backupExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(
            @Qualifier("warningQueue") Queue queue,
            @Qualifier("backupExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
