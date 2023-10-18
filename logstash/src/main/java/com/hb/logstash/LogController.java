package com.hb.logstash;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhaochengshui
 * @description
 * @date: 2023/10/18 01:05
 */
@RestController
@Slf4j
public class LogController {

    @GetMapping("/hello")
    public String hello(String name ) {
        log.info("hello: " + name +" 很高兴见到你");
        return "hello";
    }
}
