package com.hb.consumer;

import com.hb.consumer.utils.RocketMQReceiveUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/25 23:46
 */
@Slf4j
public class ConsumerApiTest {
    @Test
    public void test() throws MQClientException, InterruptedException {

        DefaultMQPushConsumer defaultConsumer = new DefaultMQPushConsumer("default_consumer");
        defaultConsumer.setNamesrvAddr("localhost:9876");
        defaultConsumer.subscribe("delay_topic", "*");
        defaultConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                log.info("接收到消息：{}", msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultConsumer.start();

        while (true){}
    }

    @Test
    public void test2() throws MQClientException {
        RocketMQReceiveUtil.ConsumerProperties consumerProperties = new RocketMQReceiveUtil.ConsumerProperties();
        consumerProperties.setConsumerGroup("default_consumer");
        consumerProperties.setTopic("delay_topic");
        consumerProperties.setNameServerAddr("localhost:9876");
        consumerProperties.setMessageSelector(MessageSelector.byTag("tag1"));
        consumerProperties.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                log.info("接收到消息：{}", msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        RocketMQReceiveUtil.createDefaultMQPushConsumer(consumerProperties);
        while (true){}
    }
}
