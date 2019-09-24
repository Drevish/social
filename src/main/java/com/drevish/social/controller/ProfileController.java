package com.drevish.social.controller;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping("/profile")
public class ProfileController extends ControllerWithUserInfo {
    @Value("${view.profile}")
    private String profileView;

    @Value("${view.other-profile}")
    private String otherProfileView;

    @Value("${path.profile}")
    private String profilePath;

    @Autowired
    private UserService userService;

    @ModelAttribute
    private User user(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }
    
    @GetMapping
    public String showOwnProfile() {
        return profileView;
    }

    @GetMapping("/id{userId}")
    public String showUserProfile(@PathVariable Long userId, Principal principal, Model model) {
        User user = null;
        try {
            user = userService.getUserById(userId);
            if (user.getEmail().equals(principal.getName())) {
                return "redirect:" + profilePath;
            }
        } catch (UserNotFoundException e) {
            log.warn("User not found with id " + userId);
        }

        model.addAttribute("user", user);
        return otherProfileView;
    }
}
