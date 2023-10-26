package com.hb.consumer;

/**
 * @author: zhaochengshui
 * @description MQ常量
 * @date: 2023/10/19 09:59
 */
public class RocketConstants {

    /**
     * 创建订单topic
     */
    public static final String CREATE_ORDER_TOPIC = "create_order_topic";
    /**
     * 创建订单消费者组
     */
    public static final String CREATE_ORDER_CONSUMER_GROUP = "${spring.application.name}|create_order|consumer";


    /**
     * 支付订单topic
     */
    public static final String PAY_ORDER_TOPIC = "pay_order_topic";
    /**
     * 创建订单消费者组
     */
    public static final String PAY_ORDER_CONSUMER_GROUP = "${spring.application.name}|pay_order|consumer";


}
