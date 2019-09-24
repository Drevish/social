package com.drevish.social.controller;

import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
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

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping("/edit")
public class EditController extends ControllerWithUserInfo {
    @Value("${view.edit}")
    private String editView;

    @Value("${path.edit}")
    private String editPath;

    @Autowired
    private EditService editService;

    @GetMapping
    public String edit() {
        return editView;
    }

    @PostMapping
    public String update(@ModelAttribute UserInfo userInfo, Principal principal, Model model) {
        User user = getCurrentUser(principal);

        if (causesValidationException(model, () -> editService.updateInfo(user, userInfo))) {
            return editView;
        }

        log.info("User with email" + user.getEmail() + " changed info");
        return "redirect:" + editPath + "?success";
    }
}
