package com.drevish.social.controller;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.EditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/edit")
public class EditController implements ValidationExceptionHandling {
    @Autowired
    private EditService editService;

    @GetMapping
    public String edit(Model model) {
        model.addAttribute("userInfo", new UserInfo());
        return "edit";
    }

    @PostMapping
    public String update(@ModelAttribute UserInfo info, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        editService.updateInfo(user, info);
        return "redirect:edit?success";
    }
}
