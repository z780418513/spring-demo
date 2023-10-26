package com.hb.consumer.listener;

/**
 * @author: zhaochengshui
 * @description 有序的消费者
 * @date: 2023/10/19 10:09
 */

import com.alibaba.fastjson.JSON;
import com.hb.consumer.Order;
import com.hb.consumer.RocketConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(
        topic = RocketConstants.PAY_ORDER_TOPIC,
        consumerGroup = RocketConstants.PAY_ORDER_CONSUMER_GROUP,
        consumeMode = ConsumeMode.ORDERLY) // 有序
public class PayOrderOrderlyListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        Order order = JSON.parseObject(message, Order.class);
        log.info("支付订单, id:{} ", order.getId());
    }
}
