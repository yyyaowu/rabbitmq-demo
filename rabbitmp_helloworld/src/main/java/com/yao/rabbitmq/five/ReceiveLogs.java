package com.yao.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.five
 * @className: ReceiveLogs
 * @author: yao
 * @description: TODO
 * @date: 2022/4/12 16:51
 * @version: 1.0
 */
public class ReceiveLogs {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        //
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 临时队列
        // 消费者断开就删除
        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("1等待接收消息");

        DeliverCallback deliverCallback =
                (consumerTag, message) -> {
                    System.out.println("1接收到的消息" + new String(message.getBody()));
                };

        channel.basicConsume(queue, true, deliverCallback, consumer -> {});
    }
}
