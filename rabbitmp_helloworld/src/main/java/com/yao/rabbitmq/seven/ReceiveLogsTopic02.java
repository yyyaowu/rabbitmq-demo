package com.yao.rabbitmq.seven;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.seven
 * @className: ReceiveLogsTopic02
 * @author: yao
 * @description: 消费者2
 * @date: 2022/4/15 15:25
 * @version: 1.0
 */
public class ReceiveLogsTopic02 {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        // 声明 Q2 队列与绑定关系
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("q2等待接收消息	");
        DeliverCallback deliverCallback =
                (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(
                            "接收队列:"
                                    + queueName
                                    + "绑定键:"
                                    + delivery.getEnvelope().getRoutingKey()
                                    + ",消息:"
                                    + message);
                };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
