package com.hb.producer.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;


/**
 * @author: zhaochengshui
 * @description RocketMQ发送工具类，普通生产者尽量复用，而事务生产者需要单例
 * @date: 2023/10/25 00:17
 */
@Slf4j
public class RocketMQSendUtil {

    private String nameServer = "127.0.0.1:9876";

    private String producerGroup = "default_producer";

    private int sendTimeOut = 30000;

    public static final MessageQueueSelector HASH_SELECTOR = new SelectMessageQueueByHash();

    public RocketMQSendUtil() {
    }

    public RocketMQSendUtil(String nameServer) {
        this.nameServer = nameServer;
    }

    public RocketMQSendUtil(String nameServer, String producerGroup) {
        this.nameServer = nameServer;
        this.producerGroup = producerGroup;
    }

    public RocketMQSendUtil(String nameServer, String producerGroup, int sendTimeOut) {
        this.nameServer = nameServer;
        this.producerGroup = producerGroup;
        this.sendTimeOut = sendTimeOut;
    }

    /**
     * 创建默认mq生产者
     *
     * @return DefaultMQProducer
     */
    private DefaultMQProducer createDefaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(sendTimeOut);
        producer.start();
        return producer;
    }

    /**
     * 创建事务MQ生产者
     *
     * @param transactionListener 事务监听器
     * @return DefaultMQProducer
     */
    public TransactionMQProducer createTransactionProducer(TransactionListener transactionListener) throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setSendMsgTimeout(sendTimeOut);
        producer.setTransactionListener(transactionListener);
        producer.start();
        return producer;
    }

    /**
     * 创建默认MQ生产者
     *
     * @return DefaultMQProducer
     * @throws MQClientException MQClientException
     */
    public DefaultMQProducer createDefaultProducer() throws MQClientException {
        DefaultMQProducer mqProducer;
        mqProducer = createDefaultMQProducer();
        return mqProducer;
    }


    /**
     * 销毁
     */
    public void shutDownNow(MQProducer mqProducer) {
        if (mqProducer != null) {
            mqProducer.shutdown();
        }
    }

}



