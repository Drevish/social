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

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public String settings() {
        return "settings";
    }

    @PostMapping(params = "changed=password")
    public String changeEmail(@RequestParam String password, @RequestParam String passwordNew,
                              @RequestParam String passwordNewRepeat, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (passwordNew == null || !passwordNew.equals(passwordNewRepeat)) {
            model.addAttribute("user", user);
            model.addAttribute("error", "New password and repeated password don't match!");
            return "settings";
        }

        settingsService.changePassword(user, password, passwordNew);
        return "settings";
    }

    @PostMapping(params = "changed=email")
    public String changeEmail(@RequestParam String email, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        settingsService.changeEmail(user, email);

        return "settings";
    }
}
