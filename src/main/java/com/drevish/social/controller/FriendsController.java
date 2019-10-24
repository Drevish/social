package com.drevish.social.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/friends")
//TODO: test
public class FriendsController extends ControllerWithUserInfo {
    @Value("${view.friends}")
    private String friendsView;

    @GetMapping
    public String show() {
        return friendsView;
    }
}
