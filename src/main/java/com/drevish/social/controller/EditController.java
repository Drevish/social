package com.drevish.social.controller;

import com.drevish.social.controller.dto.UserInfo;
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

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/edit")
public class EditController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String edit(Model model) {
        model.addAttribute("userInfo", new UserInfo());
        return "edit";
    }

    @PostMapping
    public String update(@ModelAttribute UserInfo info, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            userService.updateInfo(user, info);
        } catch (UserValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "edit";
        }

        return "redirect:edit?success";
    }
}
