package com.yao.springboot_rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.springboot_rabbitmq.config
 * @className: MyCallBack
 * @author: yao
 * @description: TODO
 * @date: 2022/4/22 10:35
 * @version: 1.0
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    /** 交换机不管是否收到消息的一个回调方法 CorrelationData 消息相关数据 ack 交换机是否收到消息 */
    @Autowired private RabbitTemplate rabbitTemplate;
    // 依赖注入rabbitTemplate之后再设置它的回调对象
    @PostConstruct
    public void init() {

        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到id为:{}的消息", id);
        } else {
            log.info("交换机还未收到id为:{}消息,由于原因:{}", id, cause);
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error(
                "消息{},被交换机{}退回,退回原因: {},路由key：{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}
