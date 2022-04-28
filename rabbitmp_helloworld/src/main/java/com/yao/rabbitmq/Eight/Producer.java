package com.yao.rabbitmq.Eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yao.rabbitmq.utils.RabbitMqUtils;

/**
 * @ProjectName: rabbitmq-demo
 *
 * @package: com.yao.rabbitmq.Eight
 * @className: Producer
 * @author: yao
 * @description: 死信队列生产者 1.ttl过期，2.超过最大长度，3.消息被拒绝
 * @date: 2022/4/21 13:24
 * @version: 1.0
 */
public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] argv) throws Exception {
        try (Channel channel = RabbitMqUtils.getChannel()) {
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 设置消息的TTL时间
            //            1.验证超过ttl时间进入死信队列,在publish参数3设置
            //            AMQP.BasicProperties properties =
            //                    new AMQP.BasicProperties().builder().expiration("10000").build();
            // 该信息是用作演示队列个数限制
            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());
                System.out.println("生产者发送消息:" + message);
            }
        }
    }
}
