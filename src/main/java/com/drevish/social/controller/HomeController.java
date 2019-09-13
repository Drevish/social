package com.drevish.social.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Value("${view.home}")
    private String homeView;

    @Value("${path.profile}")
    private String profilePath;

    @GetMapping
    public String home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return homeView;
        }
        return "redirect:" + profilePath;
    }
}
