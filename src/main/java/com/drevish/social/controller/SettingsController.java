package com.drevish.social.controller;

import com.drevish.social.controller.dto.EmailDto;
import com.drevish.social.controller.dto.PasswordDto;
import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.SettingsService;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    private UserService userService;

    @ModelAttribute
    private EmailDto emailDto(Principal principal) {
        return new EmailDto(principal.getName());
    }

    @ModelAttribute
    private PasswordDto passwordDto() {
        return new PasswordDto();
    }

    @GetMapping
    public String settings() {
        return settingsView;
    }

    @PostMapping(params = "changed=password")
    public String changePassword(@RequestParam String passwordOld, @ModelAttribute @Valid PasswordDto passwordNew,
                                 Errors errors, @RequestParam String passwordNewRepeat,
                                 Principal principal, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("error", errors.getAllErrors().get(0).getDefaultMessage());
            return settingsView;
        }

        User user = userService.getUserByEmail(principal.getName());
        // verify that new password and repeated new password are equals
        if (passwordNew.getPassword() == null || !passwordNew.getPassword().equals(passwordNewRepeat)) {
            model.addAttribute("error", "New password and repeated password don't match!");
            return settingsView;
        }

        try {
            settingsService.changePassword(user, passwordOld, passwordNew.getPassword());
        } catch (InvalidPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return settingsView;
        }

        log.info("User with email" + user.getEmail() + " changed password");
        return "redirect:" + settingsPath + "?success";
    }

    @PostMapping(params = "changed=email")
    public String changeEmail(@ModelAttribute @Valid EmailDto email, Errors errors, Principal principal, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("error", errors.getAllErrors().get(0).getDefaultMessage());
            return settingsView;
        }

        User user = userService.getUserByEmail(principal.getName());
        settingsService.changeEmail(user, email.getEmail());

        return "redirect:" + settingsPath + "?success";
    }
}
