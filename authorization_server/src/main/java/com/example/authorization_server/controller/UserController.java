package com.example.authorization_server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo {这里必须加注释}
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/20 16:12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/test")
    public String test() {
        return "hello,test";
    }

    @RequestMapping("/getAll")
    public String getAll() {
        return "hello,all";
    }
}
