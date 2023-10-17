package com.hb.skywalking.ctl;

import com.hb.skywalking.pojo.AlarmMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/17 08:59
 */
@RestController
@Slf4j
public class AlertController {

    @PostMapping("/alert")
    public String alert(@RequestBody List<AlarmMessage> alarmMessage) {
        log.info("告警信息 :{} ", alarmMessage);
        return "true";
    }
}
