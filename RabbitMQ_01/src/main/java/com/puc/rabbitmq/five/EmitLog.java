package com.puc.rabbitmq.five;

import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner s =  new Scanner(System.in);

        while (s.hasNext()) {
            String msg = s.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
            System.out.println("生产者发出消息：" + msg);
        }

    }

}
