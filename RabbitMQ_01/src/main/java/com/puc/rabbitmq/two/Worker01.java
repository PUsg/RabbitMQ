package com.puc.rabbitmq.two;


import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程
 */
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (Tag,msg) -> {
            System.out.println("接收到的消息" + new String(msg.getBody()));
        };
        CancelCallback cancelCallback = (Tag) -> {
            System.out.println(Tag + "取消消费接口回调逻辑");
        };

        /**
         * 消费者 消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        System.out.println("C3等待接收消息......");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);

    }

}
