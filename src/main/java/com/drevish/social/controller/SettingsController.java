package com.drevish.social.controller;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping("/settings")
public class SettingsController extends ControllerWithUserInfo {
    @Value("${view.settings}")
    private String settingsView;

    @Value("${path.settings}")
    private String settingsPath;

    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public String settings() {
        return settingsView;
    }

    @PostMapping(params = "changed=password")
    public String changePassword(@RequestParam String password, @RequestParam String passwordNew,
                                 @RequestParam String passwordNewRepeat, Principal principal, Model model) {
        User user = getCurrentUser(principal);

        // verify that new password and repeated new password are equals
        if (passwordNew == null || !passwordNew.equals(passwordNewRepeat)) {
            model.addAttribute("error", "New password and repeated password don't match!");
            return settingsView;
        }

        try {
            if (causesValidationException(model, () -> settingsService.changePassword(user, password, passwordNew))) {
                return settingsView;
            }
        } catch (InvalidPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return settingsView;
        }

        log.info("User with email" + user.getEmail() + " changed password");
        return "redirect:" + settingsPath + "?success";
    }

    @PostMapping(params = "changed=email")
    public String changeEmail(@RequestParam String email, Principal principal, Model model) {
        if (causesValidationException(model, () -> settingsService.changeEmail(getCurrentUser(principal), email))) {
            return settingsView;
        }
        return "redirect:" + settingsPath + "?success";
    }
}
