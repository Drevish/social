package com.drevish.social.controller;

import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/friends")
//TODO: test
public class FriendsController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String show(Principal principal, Model model) {
        //TODO: move to view.properties\
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        return "friends";
    }
}
