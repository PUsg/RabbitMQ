package com.puc.rabbitmq.three;


import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答时是不丢失的，放回队列中重新消费。
 */
public class Task02 {

    public static final String task_queue_name = "ack_queue";

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
        boolean durable = true;
        channel.queueDeclare(task_queue_name,durable,false,false,null);

        Scanner s = new Scanner(System.in);
        while(s.hasNext()){
            String msg = s.next();
            channel.basicPublish("",task_queue_name, MessageProperties.PERSISTENT_BASIC,msg.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + msg);
        }
    }

}
