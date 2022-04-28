package com.yao.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.two
 * @className: Worker01
 * @author: yao
 * @description: 一个工作线程
 * @date: 2022/4/10 16:46
 * @version: 1.0
 */
public class Worker01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback =
                (consumerTag, message) -> {
                    System.out.println("接收到的消息" + new String(message.getBody()));
                };

        CancelCallback cancelCallback =
                (consumerTag) -> {
                    System.out.println(consumerTag + "消息被消费者取消");
                };
        System.out.println("c2。。。。。");

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
