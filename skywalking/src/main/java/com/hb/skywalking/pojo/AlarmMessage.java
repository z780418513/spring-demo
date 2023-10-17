package com.hb.skywalking.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmMessage implements Serializable {
    private String scopeId;
    private String scope;
    private String name;
    private String id0;
    private String id1;
    private String ruleName;
    //告警的消息
    private String alarmMessage;
    //告警的产生时间
    private Long startTime;
 
}
