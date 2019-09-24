package com.drevish.social.controller;

import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String edit() {
        return editView;
    }

    @PostMapping
    public String update(@ModelAttribute @Valid UserInfo userInfo, Errors errors, Principal principal, Model model) {
        if (errors.hasErrors()) {
            // TODO: change to normal usage and in template too
            model.addAttribute("error", errors.toString());
            return editView;
        }

        User user = userService.getUserByEmail(principal.getName());
        userInfoService.saveForUser(userInfo, user);

        log.info("User with email" + user.getEmail() + " changed info");
        return "redirect:" + editPath + "?success";
    }
}
