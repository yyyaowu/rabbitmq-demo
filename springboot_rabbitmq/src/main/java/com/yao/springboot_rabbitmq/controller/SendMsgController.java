package com.yao.springboot_rabbitmq.controller;

import com.yao.springboot_rabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.controller
 * @className: SendMsgController
 * @author: yao
 * @description: 发送消息(生产者)
 * @date: 2022/4/21 15:22
 * @version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间:{},发送一条消息给两个队列:{}", new Date().toString(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列:" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列:" + message);
    }

    // 开始发消息 消息 ttl
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间:{},发送一条时长{}毫秒ttl信息给队列QC:{}", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend(
                "X",
                "XC",
                message,
                msg -> {
                    msg.getMessageProperties().setExpiration(ttlTime);
                    return msg;
                });
    }

    @GetMapping("sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(
                DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                message,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
        log.info("当前时间：{},发送一条延迟{}毫秒的信息给队列delayed.queue:{}", new Date(), delayTime, message);
    }
}
