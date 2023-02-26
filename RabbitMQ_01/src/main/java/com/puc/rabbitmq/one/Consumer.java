package com.puc.rabbitmq.one;


import com.rabbitmq.client.*;
import lombok.val;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者  接受消息
 */
public class Consumer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.14.45.4");
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();

        //接收消息时的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("消息消费被中断");
        };

        /**
         * 消费者 消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback, cancelCallback);

    }
}
