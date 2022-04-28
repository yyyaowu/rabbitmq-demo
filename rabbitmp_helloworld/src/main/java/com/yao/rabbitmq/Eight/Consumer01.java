package com.yao.rabbitmq.Eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.Eight
 * @className: Consumer01
 * @author: yao
 * @description: 死信队列消费者一
 * @date: 2022/4/21 13:32
 * @version: 1.0
 */
public class Consumer01 {
    // 普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        // 声明死信和普通交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        // 声明死信队列
        String deadQueue = "dead-queue";

        channel.queueDeclare(deadQueue, false, false, false, null);
        // 死信队列绑定死信交换机与routingkey
        channel.queueBind(deadQueue, DEAD_EXCHANGE, "lisi");
        // 正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        // 正常队列设置死信交换机 参数key是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 正常队列设置死信routing-key 参数key是固定值
        params.put("x-dead-letter-routing-key", "lisi");

        //        2.验证超过队列长度进入死信队列
        //        params.put("x-max-length", 6);
        String normalQueue = "normal-queue";
        channel.queueDeclare(normalQueue, false, false, false, params);
        channel.queueBind(normalQueue, NORMAL_EXCHANGE, "zhangsan");
        System.out.println("等待接收消息.....");
        //        DeliverCallback deliverCallback =
        //                (consumerTag, delivery) -> {
        //                    String message = new String(delivery.getBody(), "UTF-8");
        //                    System.out.println("Consumer01接收到消息" + message);
        //                };
        // channel.basicConsume(normalQueue, true, deliverCallback, consumerTag -> {});

        //        3.模拟消息被拒绝，验证消息被拒绝进入死信队列
        DeliverCallback deliverCallback =
                (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    if (message.equals("info5")) {
                        System.out.println("Consumer01接收到消息" + message + "并拒绝签收该消息");
                        // requeue设置为false 代表拒绝重新入队 该队列如果配置了死信交换机将发送到死信队列中
                        channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                    } else {
                        System.out.println("Consumer01接收到消息" + message);
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    }
                };
        boolean autoAck = false;
        channel.basicConsume(normalQueue, autoAck, deliverCallback, consumerTag -> {});
    }
}
