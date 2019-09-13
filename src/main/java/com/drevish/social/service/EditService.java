package com.drevish.social.service;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.model.entity.User;

import javax.validation.Valid;

public interface EditService {
    void updateInfo(User user, @Valid UserInfo info);
}
