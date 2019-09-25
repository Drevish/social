package com.drevish.social.controller;

import com.drevish.social.controller.dto.UserRegistrationInfo;
import com.drevish.social.exception.UserExistsException;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {
    @Value("${view.register}")
    private String registerView;

    @Value("${path.login}")
    private String loginPath;

    @Autowired
    private UserService userService;

    @GetMapping
    public String registrationForm(Model model) {
        model.addAttribute("userRegistrationInfo", new UserRegistrationInfo());
        return registerView;
    }

    @PostMapping
    public String registrationProcess(@Valid UserRegistrationInfo userRegistrationInfo, Errors errors,
                                      Model model) {
        String passwordCheck = userRegistrationInfo.getPasswordCheck();
        if (passwordCheck == null || !passwordCheck.equals(userRegistrationInfo.getPassword())) {
            model.addAttribute("error", "Passwords don't match!");
            return registerView;
        }

        if (errors.hasErrors()) {
            return registerView;
        }

        try {
            userService.register(userRegistrationInfo);
        } catch (UserExistsException e) {
            model.addAttribute("error", e.getMessage());
            return registerView;
        }

        log.info("New user registered with username " + userRegistrationInfo.getEmail());
        return "redirect:" + loginPath;
    }
}
