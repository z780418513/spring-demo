package com.hb.consumer.utils;

import lombok.Data;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhaochengshui
 * @description RocketMQ接收工具类
 * @date: 2023/10/26 13:02
 */
public class RocketMQReceiveUtil {

    private static final ConcurrentHashMap<String, DefaultMQPushConsumer> PUSH_CONSUMER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    private static DefaultMQPushConsumer getPushConsumer(String consumerGroup) {
        return PUSH_CONSUMER_CONCURRENT_HASH_MAP.get(consumerGroup);
    }

    private static void putPushConsumer(String consumerGroup, DefaultMQPushConsumer pushConsumer) {
        PUSH_CONSUMER_CONCURRENT_HASH_MAP.put(consumerGroup, pushConsumer);
    }

    /**
     * 创建消费者并缓存
     *
     * @param properties 消费者配置
     * @throws MQClientException mqClientException
     */
    public static void createDefaultMQPushConsumer(ConsumerProperties properties) throws MQClientException {
        DefaultMQPushConsumer pushConsumer = getPushConsumer(properties.getConsumerGroup());
        if (Objects.isNull(pushConsumer)) {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(properties.getConsumerGroup());
            consumer.setNamesrvAddr(properties.getNameServerAddr());
            consumer.subscribe(properties.getTopic(), properties.getMessageSelector());
            consumer.setMessageListener(properties.messageListener);
            consumer.start();
            putPushConsumer(properties.getConsumerGroup(), consumer);
        }
    }

    @Data
    public static class ConsumerProperties {
        private String nameServerAddr;
        private String consumerGroup;
        private String topic;
        private MessageSelector messageSelector;
        private MessageListener messageListener;
    }
}
