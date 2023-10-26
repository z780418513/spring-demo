package com.hb.producer;

import com.alibaba.fastjson.JSON;
import com.hb.producer.demo.Order;
import com.hb.producer.demo.RocketConstants;
import com.hb.producer.utils.MQSendService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class ProducerApplicationTests {

    @Test
    void syncSendOrderly() throws InterruptedException {
        String channel = "wasu";
        for (int i = 0; i < 10; i++) {
            Order order = new Order((long) i, "订单" + i, channel);
            String orderStr = JSON.toJSONString(order);
            // 发送同步消息 以相同hashkey来保证相对有序
//            MQSendService.syncSend(RocketConstants.CREATE_ORDER_TOPIC + ":" + order.getChannel(), orderStr, order.getId().toString());
            System.out.println(MQSendService.syncSend(RocketConstants.PAY_ORDER_TOPIC + ":" + order.getChannel()+"|"+"tagN", orderStr, order.getId().toString()));
//            MQSendService.asyncSend(RocketConstants.CREATE_ORDER_TOPIC + ":" + order.getChannel(), orderStr, null, new SendCallback() {
//                @Override
//                public void onSuccess(SendResult sendResult) {
//                    log.info("send success sendResult:{}",sendResult);
//                }
//
//                @Override
//                public void onException(Throwable e) {
//                    log.info("执行失败",e);
//
//                }
//            });

        }
        TimeUnit.SECONDS.sleep(100);
    }




}
