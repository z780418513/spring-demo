package com.hb.skywalking.service;

import com.hb.skywalking.pojo.ResultVo;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.stereotype.Service;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/17 09:45
 */
@Service
public class HelloService {

    @Trace
    @Tag(key = "userId", value = "arg[0]")
    @Tag(key = "userId2", value = "arg[1]")
    @Tag(key = "result.userId", value = "returnedObj.userId") //固定写法 必须写成returnedObj.XXXX
    @Tag(key = "result.userId2", value = "returnedObj.userId2")
    public ResultVo say(String userId, String userId2) {
        System.out.println("你好: ~~ " + userId + "~~" + userId2);
        return new ResultVo(userId, userId2);
    }


}


