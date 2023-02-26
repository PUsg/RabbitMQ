package com.puc.rabbitmq.three;


import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.puc.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息在自动应答时不丢失，放回队列中重新消费。
 */
public class Worker03 {

    public static final String task_queue_name = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C1处理消息较慢");

        DeliverCallback deliverCallback = (Tag, msg) -> {
            SleepUtils.sleep(1);
            System.out.println("接收到的消息：" + new String(msg.getBody()));
            //手动应答 使用信道
            channel.basicAck(msg.getEnvelope().getDeliveryTag(),false);

        };
        CancelCallback cancelCallback = (Tag) -> {
            System.out.println(Tag + "消费者取消消费 接口回调");
        };

        //设置不公平分发
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        //处理消费 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(task_queue_name,autoAck,deliverCallback,cancelCallback);
    }
}
