package com.yao.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;
import com.yao.rabbitmq.utils.SleepUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.three
 * @className: Work03
 * @author: yao
 * @description: TODO消息手动应答不丢失
 * @date: 2022/4/12 14:17
 * @version: 1.0
 */
public class Work04 {
    public static final String Task_Queue_Name = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C2等等接收消息，处理时间较长");
        DeliverCallback deliverCallback =
                (consumerTag, message) -> {
                    SleepUtils.sleep(30);
                    System.out.println("接收到的消息:" + new String(message.getBody(), "UTF-8"));
                    /** 1.消息的标记 2.是否批量应答 */
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                };

        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        channel.basicConsume(
                Task_Queue_Name,
                false,
                deliverCallback,
                (consumerTag -> {
                    System.out.println("消费者取消接收消息");
                }));
    }
}
