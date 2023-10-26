package com.hb.producer;

import com.hb.producer.utils.MessageBuilder;
import com.hb.producer.utils.RocketMQSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * 发送方式:
 * - 同步发送 会有返回值,保证发送的可靠,但是性能不太好
 * - 异步发送 没有返回值,结果异步回调,性能好
 * - 单向发送 只发送,不能保证消息的正确投递,一般用于日志收集等
 * <p>
 * - 普通消息
 * - 定时/延时消息
 * - 顺序消息
 * - 事务消息
 * - 批量消息
 *
 * @author: zhaochengshui
 * @description RocketMQ原生APi测试接口
 * @date: 2023/10/21 05:16
 */
@Slf4j
class ProducerApiTest {

    private final SendCallback sendCallback =
            new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送成功");
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                    System.out.println("发送异常");
                }
            };


    private TransactionListener transactionListener = new TransactionListener() {
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            System.out.println("执行本地事务: " + new String(msg.getBody()));
            return LocalTransactionState.UNKNOW;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            System.out.println("检查本地事务" + new String(msg.getBody()));
            return LocalTransactionState.COMMIT_MESSAGE;
        }

    };

    /**
     * 普通消息
     *
     * @throws InterruptedException
     * @throws MQClientException
     * @throws MQBrokerException
     * @throws RemotingException
     */
    @Test
    void firstProducer() throws InterruptedException, MQClientException, MQBrokerException, RemotingException {

        DefaultMQProducer producer =
                new RocketMQSendUtil("my.rocketmq.com:9876")
                        .createDefaultProducer();

        Message message = MessageBuilder
                .withPayload("Hello RocketMQ!")
                .withTags("taga")
                .withTopic("test_default_topic")
                .build();

        for (int i = 0; i < 10; i++) {
            // 同步消息
            SendResult send = producer.send(message);
            System.out.println(send);

            // 异步消息
            producer.send(message, sendCallback);

            // 单向消息
            producer.sendOneway(message);
        }

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 顺序消息
     *
     * @throws MQClientException
     * @throws InterruptedException
     * @throws MQBrokerException
     * @throws RemotingException
     */
    @Test
    void orderLy() throws MQClientException, InterruptedException, MQBrokerException, RemotingException {
        DefaultMQProducer producer =
                new RocketMQSendUtil("my.rocketmq.com:9876")
                        .createDefaultProducer();

        Message message = MessageBuilder
                .withPayload("Hello RocketMQ!")
                .withTags("taga")
                .withTopic("test_default_topic")
                .build();

        for (int i = 0; i < 10; i++) {
            // 同步消息
            SendResult send = producer.send(message, RocketMQSendUtil.HASH_SELECTOR, 111);
            System.out.println(send);

            // 异步消息
            producer.send(message, RocketMQSendUtil.HASH_SELECTOR, 111, sendCallback);

            // 单向消息
            producer.sendOneway(message, RocketMQSendUtil.HASH_SELECTOR, 111);
        }

        TimeUnit.SECONDS.sleep(10);
    }


    /**
     * 事务消息
     *
     * @throws MQClientException
     * @throws InterruptedException
     */
    @Test
    void transaction() throws MQClientException, InterruptedException {
        TransactionMQProducer transactionProducer =
                new RocketMQSendUtil("my.rocketmq.com:9876")
                        .createTransactionProducer(transactionListener);

        Message message = MessageBuilder
                .withPayload("Hello RocketMQ!")
                .withTags("taga")
                .withTopic("test_default_topic")
                .build();


        for (int i = 0; i < 2; i++) {
            // 同步消息
            TransactionSendResult transactionSendResult = transactionProducer.sendMessageInTransaction(message, null);
            System.out.println(transactionSendResult);
        }


        TimeUnit.SECONDS.sleep(100);
    }

    /**
     * 批量消息，只是发送的时候指定批量消息集合就行，不能异步
     *
     * @throws MQClientException
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     */
    @Test
    void batchMessage() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer =
                new RocketMQSendUtil("my.rocketmq.com:9876")
                        .createDefaultProducer();

        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = MessageBuilder
                    .withPayload("Hello RocketMQ!" + i)
                    .withTags("taga")
                    .withTopic("test_default_topic")
                    .build();
            messages.add(message);
        }

        SendResult send = producer.send(messages);
        System.out.println(send);

        TimeUnit.SECONDS.sleep(10);
    }

    /**
     * 延时消息
     * @throws MQClientException
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws InterruptedException
     */
    @Test
    void delay() throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer =
                new RocketMQSendUtil("my.rocketmq.com:9876")
                        .createDefaultProducer();

        Message message = MessageBuilder
                .withPayload("Hello RocketMQ!")
                .withTags("tag1")
                .withTopic("delay_topic")
                .withDelayTimeLevel(3)
                .build();

        Message message2 = MessageBuilder
                .withPayload("Hello RocketMQ!")
                .withTags("taga")
                .withTopic("delay_topic")
                .withDeliverTimeMs(1000)
                .build();

        SendResult send = producer.send(message);
        log.info("发送结果: {}", send);

        SendResult send2 = producer.send(message2);
        log.info("发送结果: {}", send2);

        TimeUnit.SECONDS.sleep(10);

    }

}
