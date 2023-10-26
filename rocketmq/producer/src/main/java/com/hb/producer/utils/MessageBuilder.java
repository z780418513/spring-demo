package com.hb.producer.utils;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageConst;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/25 20:32
 */
public class MessageBuilder<T> {
    private T payload;
    private String topic;
    private String tags;
    private Map<String, String> properties;

    private MessageBuilder(T payload) {
        this.payload = payload;
    }


    public static <T> MessageBuilder<T> withPayload(T payload) {
        return new MessageBuilder<>(payload);
    }

    public MessageBuilder<T> withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder<T> withTags(String tags) {
        this.tags = tags;
        return this;
    }

    public MessageBuilder<T> withDelayTimeLevel(int level) {
        this.putProperty(MessageConst.PROPERTY_DELAY_TIME_LEVEL, String.valueOf(level));
        return this;
    }

    public MessageBuilder<T> withDelayTimeSec(long sec) {
        this.putProperty(MessageConst.PROPERTY_TIMER_DELAY_SEC, String.valueOf(sec));
        return this;
    }

    public MessageBuilder<T> withDelayTimeMs(long timeMs) {
        this.putProperty(MessageConst.PROPERTY_TIMER_DELAY_MS, String.valueOf(timeMs));
        return this;
    }

    public MessageBuilder<T> withDeliverTimeMs(long timeMs) {
        this.putProperty(MessageConst.PROPERTY_TIMER_DELIVER_MS, String.valueOf(timeMs));
        return this;
    }

    public MessageBuilder<T> withProperty(String key, String value) {
        this.putProperty(key, value);
        return this;
    }


    public Message build() {
        Message message = new Message(topic, tags, JSON.toJSONString(payload).getBytes());
        if (null != properties && properties.size() > 0) {
            Map<String, String> messageProperties = message.getProperties();
            messageProperties.putAll(properties);
        }
        return message;
    }

    void putProperty(final String name, final String value) {
        if (null == this.properties) {
            this.properties = new HashMap<>();
        }

        this.properties.put(name, value);
    }

}
