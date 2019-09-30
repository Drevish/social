package com.drevish.social.controller;

import com.drevish.social.exception.InvalidImageException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping("/profile")
public class ProfileController extends ControllerWithUserInfo {
    @Value("${view.profile}")
    private String profileView;

    @Value("${view.other-profile}")
    private String otherProfileView;

    @Value("${path.profile}")
    private String profilePath;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showOwnProfile(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        return profileView;
    }

    @GetMapping("/id{userId}")
    public String showUserProfile(@PathVariable Long userId, Principal principal, Model model) {
        User user;
        try {
            user = userService.getUserById(userId);
        } catch (UserNotFoundException e) {
            log.warn("User not found with id " + userId);
            return "redirect:" + profilePath;
        }

        if (user.getEmail().equals(principal.getName())) {
            return "redirect:" + profilePath;
        }

        UserInfo userInfo = userInfoService.getUserInfoByEmail(user.getEmail());
        model.addAttribute("userInfo", userInfo);

        return otherProfileView;
    }

    @PostMapping(params = "change=upload-image")
    public String updateImage(@RequestParam MultipartFile image, Principal principal, Model model) {
        try {
            userInfoService.setImage(image, userInfo(principal));
        } catch (InvalidImageException e) {
            log.warn(e.toString());
        }
        return showOwnProfile(principal, model);
    }

    @PostMapping(params = "change=delete-image")
    public String deleteImage(Principal principal, Model model) {
        UserInfo userInfo = userInfo(principal);
        userInfoService.deleteImage(userInfo);
        return showOwnProfile(principal, model);
    }
}
