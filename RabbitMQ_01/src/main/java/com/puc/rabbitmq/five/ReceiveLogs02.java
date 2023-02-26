package com.puc.rabbitmq.five;

import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs02 {
    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        //临时队列
        String queueName = channel.queueDeclare().getQueue();

        /**
         * 绑定交换机和队列
         */
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息,把接收到的消息打印在屏幕 ...........");


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02控制台接收到的消息：" + message.toString());
        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
