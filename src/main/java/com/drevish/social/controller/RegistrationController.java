package com.drevish.social.controller;

import com.drevish.social.exception.UserExistsException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
@Slf4j
public class RegistrationController {

  @Autowired
  private UserService userService;

  @GetMapping
  public String registrationForm(Model model) {
    model.addAttribute("user", new User());
    return "register";
  }

  @PostMapping
  public String registrationProcess(@Valid User user, Errors errors,
                                    @ModelAttribute String password_check, Model model) {
    if (password_check == null || password_check.equals(user.getPassword())) {
      model.addAttribute("error", "Passwords don't match!");
      return "register";
    }

    if (errors.hasErrors()) {
      return "register";
    }

    try {
      userService.register(user);
    } catch (UserExistsException e) {
      model.addAttribute("error", e.getMessage());
      return "register";
    }

    return "redirect:/login";
  }
}
