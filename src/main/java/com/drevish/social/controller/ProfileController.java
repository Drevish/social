package com.drevish.social.controller;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String showOwnProfile(Authentication authentication, Model model, HttpSession session) {
        //TODO: fix that when we start not from profile page session user is empty
        if (session.getAttribute("user") == null) {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            session.setAttribute("user", user);
        }

        return "profile";
    }

    @GetMapping("/id{userId}")
    public String showUserProfile(@PathVariable Long userId, Authentication authentication, Model model) {
        User user = null;
        try {
            user = userService.getUserById(userId);
            if (user.getEmail().equals(authentication.getName())) {
                return "redirect:/profile";
            }
        } catch (UserNotFoundException ignored) {
        }

        model.addAttribute("user", user);
        return "user_profile";
    }
}
