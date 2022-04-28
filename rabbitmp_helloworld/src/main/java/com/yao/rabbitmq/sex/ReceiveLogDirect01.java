package com.yao.rabbitmq.sex;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.sex
 * @className: ReceiveLogDirect01
 * @author: yao
 * @description: TODO
 * @date: 2022/4/12 17:08
 * @version: 1.0
 */
public class ReceiveLogDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare("console", false, false, false, null);
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        DeliverCallback deliverCallback =
                (consumerTag, message) -> {
                    System.out.println("1接收到的消息" + new String(message.getBody()));
                };

        channel.basicConsume("console", true, deliverCallback, consumerTag -> {});
    }
}
