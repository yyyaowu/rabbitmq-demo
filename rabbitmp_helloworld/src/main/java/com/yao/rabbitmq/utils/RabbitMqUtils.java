package com.yao.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @projectName: rabbitmq-demo
 * @package: com.yao.rabbitmq.utils
 * @className: RabbitMqUtils
 * @author: yao
 * @description: TODO
 * @date: 2022/4/10 16:43
 * @version: 1.0
 */
public class RabbitMqUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
	ConnectionFactory connectionFactory = new ConnectionFactory();
	connectionFactory.setHost("192.168.209.18");
	connectionFactory.setUsername("admin");
	connectionFactory.setPassword("123");
	Connection connection = connectionFactory.newConnection();
	Channel channel = connection.createChannel();
	return channel;
    }
}
