package com.drevish.social.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
  @GetMapping
  public String home(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return "home";
    }
    return "redirect:/profile";
  }
}
