package com.example.resource_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo {这里必须加注释}
 *
 * @author canjiechen
 * @version 2.0.0
 * @date 2022/12/18 17:26
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/test")
    public String test() {
        return "hello, world";
    }
}
