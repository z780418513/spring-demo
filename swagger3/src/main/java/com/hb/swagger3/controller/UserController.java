package com.hb.swagger3.controller;


import com.hb.swagger3.api.UserControllerApi;
import com.hb.swagger3.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController implements UserControllerApi {

    @PostMapping("/add/{name}")
    @Override
    public User addUser(@PathVariable String name) {
        return new User(name, 18);
    }

    @GetMapping("/del/{name}")
    @Override
    public void delUser(@PathVariable String name) {
        log.info("删除name={}的用户", name);
    }
}

