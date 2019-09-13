package com.drevish.social.controller;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Value("${view.profile}")
    private String profileView;

    @Value("${view.other-profile}")
    private String otherProfileView;

    @Value("${path.profile}")
    private String profilePath;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showOwnProfile() {
        return profileView;
    }

    @GetMapping("/id{userId}")
    public String showUserProfile(@PathVariable Long userId, Authentication authentication, Model model) {
        User user = null;
        try {
            user = userService.getUserById(userId);
            if (user.getEmail().equals(authentication.getName())) {
                return "redirect:" + profilePath;
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

        model.addAttribute("user", user);
        return otherProfileView;
    }
}
