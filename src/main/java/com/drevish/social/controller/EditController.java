package com.drevish.social.controller;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.EditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequestMapping("/edit")
public class EditController implements ValidationExceptionHandling {
    @Value("${view.edit}")
    private String editView;

    @Value("${path.edit}")
    private String editPath;

    @Autowired
    private EditService editService;

    @GetMapping
    public String edit(Model model) {
        model.addAttribute("userInfo", new UserInfo());
        return editView;
    }

    @PostMapping
    public String update(@ModelAttribute UserInfo info, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        editService.updateInfo(user, info);
        log.info("User with email" + user.getEmail() + " changed info");
        return "redirect:" + editPath + "?success";
    }
}
