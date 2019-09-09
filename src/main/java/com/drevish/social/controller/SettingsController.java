package com.drevish.social.controller;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.SettingsService;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @Autowired
    private UserService userService;

    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public String settings() {
        return "settings";
    }

    @PostMapping(params = "changed=password")
    public String changePassword(@RequestParam String password, @RequestParam String passwordNew,
                                 @RequestParam String passwordNewRepeat, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        // verify that new password and repeated new password are equals
        if (passwordNew == null || !passwordNew.equals(passwordNewRepeat)) {
            model.addAttribute("error", "New password and repeated password don't match!");
            return "settings";
        }

        try {
            settingsService.changePassword(user, password, passwordNew);
        } catch (InvalidPasswordException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "settings";
    }

    @PostMapping(params = "changed=email")
    public String changeEmail(@RequestParam String email, Model model,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        settingsService.changeEmail(user, email);
        return "settings";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public String validationError(ConstraintViolationException e, Model model) {
        String errorMessage = e.getConstraintViolations().stream().findFirst().get().getMessage();
        model.addAttribute("error", errorMessage);
        return "settings";
    }
}
