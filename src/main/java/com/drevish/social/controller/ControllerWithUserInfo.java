package com.drevish.social.controller;

import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

public abstract class ControllerWithUserInfo extends MyController {
    @Autowired
    protected UserService userService;

    @ModelAttribute(name = "user")
    public User getCurrentUser(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }
}
