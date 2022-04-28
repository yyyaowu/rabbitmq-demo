package com.yao.springboot_rabbitmq.consume;

import com.rabbitmq.client.Channel;
import com.yao.springboot_rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.consume
 * @className: DeadLetterQueueConsumer
 * @author: yao
 * @description: TODO
 * @date: 2022/4/21 15:30
 * @version: 1.0
 */
@Component
@Slf4j
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("当前时间:{},收到死信队列的消息: {}", new Date().toString(), msg);
    }

    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延时队列的消息：{}", new Date().toString(), msg);
    }
}
