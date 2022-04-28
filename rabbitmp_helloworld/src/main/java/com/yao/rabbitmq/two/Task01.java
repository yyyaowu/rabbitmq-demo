package com.yao.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.yao.rabbitmq.utils.RabbitMqUtils;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.two
 * @className: Task01
 * @author: yao
 * @description: 生产者
 * @date: 2022/4/10 16:58
 * @version: 1.0
 */
public class Task01 {
    private static final String QUEUE_NAME = "hello";

  public static void main(String[] args) throws IOException, TimeoutException {
    //
      Channel channel = RabbitMqUtils.getChannel();

      channel.queueDeclare(QUEUE_NAME,false,false,false,null);

      Scanner scanner = new Scanner(System.in);
      while(scanner.hasNext()){
	  String message = scanner.next();
	  channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
      System.out.println("发送消息完成");
      }
  }
}
