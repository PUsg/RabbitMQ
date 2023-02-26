package com.puc.rabbitmq.sixin;

import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

public class Producer {

    static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //设置消息的TTL
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for(int i = 1; i < 11; i ++) {
            String message = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes());
            System.out.println("生产者 发送消息" + message);
        }
    }

}
