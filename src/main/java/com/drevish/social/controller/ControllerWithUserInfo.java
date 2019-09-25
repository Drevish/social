package com.drevish.social.controller;

import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

public abstract class ControllerWithUserInfo {
    @Autowired
    protected UserInfoService userInfoService;

    @ModelAttribute
    public UserInfo userInfo(Principal principal) {
        return userInfoService.getUserInfoByEmail(principal.getName());
    }
}
