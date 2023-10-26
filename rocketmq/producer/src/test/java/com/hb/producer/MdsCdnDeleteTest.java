package com.hb.producer;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/24 14:01
 */
@SpringBootTest(classes = ProducerApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MdsCdnDeleteTest {


    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void test() throws InterruptedException {
        String xml = "/var/ftp/test/mds/cdn/del/clps.xml";
        mockSendMessage(xml);
        TimeUnit.SECONDS.sleep(5);
    }

    private void mockSendMessage(String xmlRemotePath) {
        CDNDeleteStatsMessage statsMessage = new CDNDeleteStatsMessage();
        statsMessage.setXmlRemotePath(xmlRemotePath);
        Message<String> message = MessageBuilder
                .withPayload(JSON.toJSONString(statsMessage))
                .build();
        SendResult sendResult = rocketMQTemplate.syncSend("cdn_delete_stats_clps", message);
        log.info("[sendMQMessageOrderly]  message:{}, sendResult:{}", message, JSON.toJSONString(sendResult));
    }


}

@Data
class CDNDeleteStatsMessage {
    /**
     * xml远程路径
     */
    private String xmlRemotePath;

    private String redisKey;

    private String cdnExtCode;

    private String cdiExtCode;

}
