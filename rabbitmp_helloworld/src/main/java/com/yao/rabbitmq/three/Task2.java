package com.yao.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.three
 * @className: Task2
 * @author: yao
 * @description: TODO消息手动答不丢失
 * @date: 2022/4/12 14:10
 * @version: 1.0
 */
public class Task2 {

    public static final String Task_Queue_Name = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        boolean durable = true; // 需要让队列持久化

        channel.queueDeclare(Task_Queue_Name, durable, false, false, null);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(
                    "",
                    Task_Queue_Name,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("UTF-8"));
            System.out.println("消息已发出");
        }
    }
}
