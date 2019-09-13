package com.drevish.social.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Value("${view.login}")
    private String loginView;

    @GetMapping
    public String loginForm() {
        return loginView;
    }
}
