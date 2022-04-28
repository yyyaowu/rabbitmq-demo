package com.yao.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.four
 * @className: ConfirmMessage
 * @author: yao
 * @description: 发布确认模式:1.单个2.批量3.异步确认
 * @date: 2022/4/12 15:13
 * @version: 1.0
 */
public class ConfirmMessage {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //        ConfirmMessage.publishMessageIndividually();
        //        ConfirmMessage.publishMessageBatch();
        ConfirmMessage.publishMessageAsync();
    }

    public static void publishMessageIndividually()
            throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);

        channel.confirmSelect();

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + " ";
            channel.basicPublish("", queueName, null, message.getBytes());
            boolean flag = channel.waitForConfirms();

            if (flag) {
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }

    public static void publishMessageBatch()
            throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);

        channel.confirmSelect();

        int batchSize = 100;

        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + " ";
            channel.basicPublish("", queueName, null, message.getBytes());

            if (i % batchSize == 0) {
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);

        channel.confirmSelect();

        // 线程安全有序的哈希表,适用于高并发
        // 将序号与消息关联
        // 轻松批量删除条目
        // 支持高并发（多线程）
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        long begin = System.currentTimeMillis();
        // 成功 1.消息标记 2.是否为批量确认
        ConfirmCallback ackCallback =
                (deliveryTag, multiple) -> {
                    if (multiple) {
                        ConcurrentNavigableMap<Long, String> confirm =
                                outstandingConfirms.headMap(deliveryTag);
                        confirm.clear();
                    } else {
                        outstandingConfirms.remove(deliveryTag);
                    }

                    System.out.println("确认的消息" + deliveryTag);
                };

        // 失败
        ConfirmCallback nackCallback =
                (deliveryTag, multiple) -> {
                    System.out.println("未确认消息" + deliveryTag);
                };

        // 监听器，监听哪些消息发生成功了
        channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            //
            String message = "消息" + i;
            channel.basicPublish("", queueName, null, message.getBytes());
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }
}
