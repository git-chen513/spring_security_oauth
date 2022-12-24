package com.example.resource_server.controller;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
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

    @GetMapping("/user_info")
    public String getUserInfo(OAuth2Authentication authentication) {
        if (authentication != null) {
            return "yes authentication";
        }
        return "no authentication";
    }
}
