package com.yao.springboot_rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.controller
 * @className: Producer
 * @author: yao
 * @description: TODO
 * @date: 2022/4/21 20:43
 * @version: 1.0
 */
@RestController
@RequestMapping("/confirm")
@Slf4j
public class Producer {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    @Autowired private RabbitTemplate rabbitTemplate;
    //    @Autowired private MyCallBack myCallBack;
    //    // 依赖注入rabbitTemplate之后再设置它的回调对象
    //    @PostConstruct
    //    public void init() {
    //
    //        rabbitTemplate.setConfirmCallback(myCallBack);
    //        rabbitTemplate.setReturnsCallback(myCallBack);
    //    }

    @GetMapping("sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        // 指定消息id为1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey = "key1";
        rabbitTemplate.convertAndSend(
                CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData1);
        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "key2";
        rabbitTemplate.convertAndSend(
                CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData2);
        log.info("发送消息内容:{}", message);
    }
}
