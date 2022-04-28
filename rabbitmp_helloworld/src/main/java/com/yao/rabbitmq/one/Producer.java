package com.yao.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.one
 * @className: Producer
 * @author: yao
 * @description: TODO
 * @date: 2022/4/10 15:56
 * @version: 1.0
 */
public class Producer {

    public static final String QUEUE_NAME = "hello";

  public static void main(String[] args) throws IOException, TimeoutException {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("192.168.209.18");
      factory.setUsername("admin");
      factory.setPassword("123");

      Connection connection = factory.newConnection();

      Channel channel = connection.createChannel();

    /**
     * 生成队列
     * 1.队列名称
     * 2.消息是否持久化
     * 3.是否只供一个消费者消费
     * 4.是否自动删除，最后一个消费者断开链接后 是否自动删除
     * 5.其他
     */
    channel.queueDeclare(QUEUE_NAME,false,false,false,null);

    String message = "hello world";
    /**
     * 1.发送到哪个交换机
     * 2.路由的key值是哪个，本次队列的名称
     * 3.其他参数信息
     * 4.消息体
     */
    channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
    System.out.println("消息已发送");
      //
  }
}
