package com.hb.producer.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.lang.Nullable;

import java.util.Objects;

/**
 * @author: zhaochengshui
 * @description RocketMQ消息发送服务
 * @date: 2023/10/19 10:44
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class MQSendService {

    public static final String TAGS = "TAGS";

    private static RocketMQTemplate rocketMQTemplate = SpringUtil.getBean(RocketMQTemplate.class);

    /***
     * 发送同步消息
     * @param destination topic:tags
     * @param payload 消息
     * @param hashKey 唯一key
     * @return true=发送成功
     */
    public static boolean syncSend(String destination, Object payload, @Nullable String hashKey) {
        SendResult sendResult;
        if (StringUtils.isBlank(hashKey)) {
            sendResult = rocketMQTemplate.syncSend(destination, payload);
        } else {
            sendResult = rocketMQTemplate.syncSendOrderly(destination, payload, hashKey);
        }
        return parseResult(sendResult);
    }


    /**
     * 发送异步消息
     *
     * @param destination  topic:tags
     * @param payload      消息
     * @param hashKey      唯一key
     * @param sendCallback 异步消息回调
     */
    public static void asyncSend(String destination, Object payload, @Nullable String hashKey, SendCallback sendCallback) {
        if (StringUtils.isBlank(hashKey)) {
            rocketMQTemplate.asyncSend(destination, payload, sendCallback);
        } else {
            rocketMQTemplate.asyncSendOrderly(destination, payload, hashKey, sendCallback);
        }
    }

    /**
     * one-way发送消息,存在消息丢失情况,适用于日志收集
     *
     * @param destination topic:tags
     * @param payload     消息
     * @param hashKey     唯一key
     */
    public static void oneWaySend(String destination, Object payload, @Nullable String hashKey) {
        if (StringUtils.isBlank(hashKey)) {
            rocketMQTemplate.sendOneWay(destination, payload);
        } else {
            rocketMQTemplate.sendOneWayOrderly(destination, payload, hashKey);
        }
    }



    /**
     * 解析结果
     *
     * @param sendResult 消息结果
     * @return true=成功
     */
    private static boolean parseResult(SendResult sendResult) {
        if (!Objects.equals(sendResult.getSendStatus(), SendStatus.SEND_OK)) {
            log.error("send Message fail, Result: {}", sendResult);
            return false;
        }
        return true;
    }

    public static void setRocketMQTemplate(RocketMQTemplate rocketMQTemplate) {
        MQSendService.rocketMQTemplate = rocketMQTemplate;
    }

}
