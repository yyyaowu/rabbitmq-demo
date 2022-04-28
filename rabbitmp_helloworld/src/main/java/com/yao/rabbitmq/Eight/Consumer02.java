package com.yao.rabbitmq.Eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.Eight
 * @className: Consumer02
 * @author: yao
 * @description: TODO
 * @date: 2022/4/21 13:39
 * @version: 1.0
 */
public class Consumer02 {
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        String deadQueue = "dead-queue";
        channel.queueDeclare(deadQueue, false, false, false, null);
        channel.queueBind(deadQueue, DEAD_EXCHANGE, "lisi");
        System.out.println("等待接收死信队列消息.....");
        DeliverCallback deliverCallback =
                (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println("Consumer02接收死信队列的消息" + message);
                };
        channel.basicConsume(deadQueue, true, deliverCallback, consumerTag -> {});
    }
}
