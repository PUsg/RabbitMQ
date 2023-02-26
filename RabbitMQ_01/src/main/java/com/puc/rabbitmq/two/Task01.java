package com.puc.rabbitmq.two;


import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产者  发送大量消息
 */
public class Task01 {
    public static final String DEQUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 生成一个队列的参数
         * 1.队列名称
         * 2.队列里面的消息是否持久化（磁盘） 默认情况下消息存储在内存中
         * 3.队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者消费，false只能一个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接 之后 是否自动删除
         * 5.其他参数
         */
        channel.queueDeclare(DEQUE_NAME,false,false,false,null);

        //控制台接收信息
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish("", DEQUE_NAME,null,msg.getBytes());
            System.out.println("发送消息完成");
        }
    }
}
