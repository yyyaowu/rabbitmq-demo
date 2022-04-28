package com.yao.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.one
 * @className: Consumer
 * @author: yao
 * @description: TODO
 * @date: 2022/4/10 16:10
 * @version: 1.0
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.209.18");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback =
                (consumer, mesage) -> {
                    System.out.println(new String(mesage.getBody()) + "111");
                };

        CancelCallback cancelCallback =
                consumerTag -> {
                    System.out.println("消费中断");
                };
        /** 1.消费哪一个队列 2.消费成功是否自动应答 3.消费者成功消费的回调 4.消费者取录消费者的回调 */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
