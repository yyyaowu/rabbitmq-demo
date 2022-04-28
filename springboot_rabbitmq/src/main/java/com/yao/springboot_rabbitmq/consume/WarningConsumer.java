package com.yao.springboot_rabbitmq.consume;

import com.yao.springboot_rabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.consume
 * @className: WarningConsumer
 * @author: yao
 * @description: TODO
 * @date: 2022/4/22 11:38
 * @version: 1.0
 */
@Component
@Slf4j
public class WarningConsumer {
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由信息 : {}", msg);
    }
}
