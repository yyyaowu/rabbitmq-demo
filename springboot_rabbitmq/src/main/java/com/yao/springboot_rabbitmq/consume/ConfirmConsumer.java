package com.yao.springboot_rabbitmq.consume;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.yao.springboot_rabbitmq.config.ConfirmConfig.CONFIRM_QUEUE_NAME;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.consume
 * @className: ConfirmConsumer
 * @author: yao
 * @description: TODO
 * @date: 2022/4/22 10:38
 * @version: 1.0
 */
@Component
@Slf4j
public class ConfirmConsumer {

    @RabbitListener(queues = CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("接受到队列confirm.queue消息:{}", msg);
    }
}
