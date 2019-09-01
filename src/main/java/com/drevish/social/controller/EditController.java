package com.drevish.social.controller;

import com.drevish.social.exception.UserValidationException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/edit")
public class EditController {
  @Autowired
  private UserService userService;

  @GetMapping
  public String edit(Authentication authentication, Model model) {
    String email = authentication.getName();
    User user = userService.getUserByEmail(email);
    model.addAttribute("user", user);
    return "edit";
  }

  @PostMapping
  public String update(Authentication authentication,
                       @ModelAttribute User user, Model model) {
    String email = authentication.getName();
    User currentUser = userService.getUserByEmail(email);

    System.out.println(user);
    currentUser.setName(user.getName());
    currentUser.setSurname(user.getSurname());

    try {
      userService.updateInfo(currentUser);
    } catch (UserValidationException e) {
      model.addAttribute("error", e.getMessage());
      return "edit";
    }

    return "redirect:edit?success";
  }
}
