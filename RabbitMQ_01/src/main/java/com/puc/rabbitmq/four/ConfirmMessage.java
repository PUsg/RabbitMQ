package com.puc.rabbitmq.four;


import com.puc.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认模式
 * 1.单个确认
 * 2.批量确认
 * 3。异步批量处理
 */
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
//        发布1000个单独确认消息 耗费5841ms
//        发布1000个批量确认消息 耗费181ms
//        发布1000个异步确认消息 耗费30ms
        publishMessageIndividually();
        publishMessageBatch();
        publishMessageAsync();
    }

    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启确认发布
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表 适用于高并发的情况
         * 轻松的 将序号与消息进行关联
         * 轻松批量删除条目 只要给到序列号
         * 支持并发访问
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();


        /**
         * 确认收到消息的一个回调
         * 1.消息序列号
         * 2.true 可以确认小于等于当前序列号的 消息
         *   false 确认当前序列号消息
         */
        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (Tag, multiple) -> {
            ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(Tag);
        };
        //消息确认失败 回调函数
        ConfirmCallback nackCallback = (Tag, multiple) -> {};

        //准备消息的监听器 监听那些消息成功了 那些消息失败了
        channel.addConfirmListener(ackCallback,nackCallback); // 异步通知

        //开始时间
        long begin = System.currentTimeMillis();

        //批量发送消息
        for(int i = 0; i < MESSAGE_COUNT; i ++) {
            String  msg = "消息" + i;
            outstandingConfirms.put(channel.getNextPublishSeqNo(),msg);
            channel.basicPublish("",queueName,null,msg.getBytes());
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息 耗费" +(end - begin) +"ms");

    }



    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for(int i = 0; i < MESSAGE_COUNT; i ++) {
            String message = i + "";
            channel.basicPublish("",queueName,null,message.getBytes());
            channel.waitForConfirms();
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息 耗费" +(end - begin) +"ms");
    }

    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {

        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发送 批量确认
        for(int i = 0; i <= MESSAGE_COUNT; i ++) {
            String msg = i + "";
            channel.basicPublish("",queueName,null,msg.getBytes());

            //判断达到100条消息的时候 确认一次
            if(i % 100 == 0) {
                //发布确认
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息 耗费" +(end - begin) +"ms");
    }




}
