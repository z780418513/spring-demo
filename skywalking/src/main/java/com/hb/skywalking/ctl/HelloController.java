package com.hb.skywalking.ctl;

import com.hb.skywalking.pojo.ResultVo;
import com.hb.skywalking.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/16 22:31
 */
@RestController
@Slf4j
public class HelloController {
    @Resource
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello() throws InterruptedException {
        log.info("执行了hello请求-----");
        TimeUnit.SECONDS.sleep(5);
        return "hello";
    }

    @GetMapping("/hello2")
    public String hello2(@RequestParam String name) throws InterruptedException {
        ResultVo say = helloService.say(name,"admin");
        return "ok";
    }
}
