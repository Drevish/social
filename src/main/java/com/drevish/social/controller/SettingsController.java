package com.drevish.social.controller;

import com.drevish.social.model.entity.User;
import com.drevish.social.service.SettingsService;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    private UserService userService;

    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public String settings(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        return "settings";
    }

    @PostMapping(params = "changed=password")
    public String changeEmail(@RequestParam String password, @RequestParam String passwordNew,
                              @RequestParam String passwordNewRepeat, Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);

        if (passwordNew == null || !passwordNew.equals(passwordNewRepeat)) {
            model.addAttribute("user", user);
            model.addAttribute("error", "New password and repeated password don't match!");
            return "settings";
        }

        settingsService.changePassword(user, password, passwordNew);
        return "redirect:/settings";
    }

    @PostMapping(params = "changed=email")
    public String changeEmail(@RequestParam String email, Authentication authentication, Model model) {
        String userEmail = authentication.getName();
        User user = userService.getUserByEmail(userEmail);
        settingsService.changeEmail(user, email);

        // We have to log out because old authentication contains old email and can't verify user with the new one
        //TODO: should be fixed
        return "redirect:/logout";
    }
}
