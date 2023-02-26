package com.puc.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.val;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP 连接队列
        factory.setHost("1.14.45.4");
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        /**
         * 生成一个队列的参数
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况下消息存储在内存中
         * 3.队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接 之后 是否自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "Hello World";
        /**
         * 发送一个消息
         * 1.参数：发送到哪个交换机
         * 2.路由的key值是什么 本次是队列的名称
         * 3.其他
         * 4。发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");
    }


}
