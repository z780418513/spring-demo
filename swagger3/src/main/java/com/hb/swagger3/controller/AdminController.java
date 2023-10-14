package com.hb.swagger3.controller;


import com.hb.swagger3.api.AdminControllerApi;
import com.hb.swagger3.pojo.CommonResult;
import com.hb.swagger3.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController implements AdminControllerApi {


    @PostMapping("/add/{name}")
    @Override
    public CommonResult<User> addUser(@PathVariable String name) {
        return CommonResult.success(new User(name, 18));
    }

    @GetMapping("/del/{name}")
    @Override
    public CommonResult<User> delUser(@PathVariable String name) {
        log.info("管理员删除name={}的用户", name);
        return CommonResult.success(new User(name, 25));
    }

    @PostMapping("/update")
    @Override
    public CommonResult<User> updateUser(@RequestBody User user) {
        user.setAge(100);
        log.info("管理员更新{}用户的年龄为{}", user, 100);
        return CommonResult.success(user);
    }
}

