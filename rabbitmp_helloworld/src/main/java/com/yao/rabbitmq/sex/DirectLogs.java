package com.yao.rabbitmq.sex;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.sex
 * @className: DirectLogs
 * @author: yao
 * @description: TODO
 * @date: 2022/4/12 17:16
 * @version: 1.0
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息" + message);
        }
    }
}
