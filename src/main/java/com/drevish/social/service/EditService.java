package com.drevish.social.service;

import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;

import javax.validation.Valid;

public interface EditService {
    void updateInfo(User user, @Valid UserInfo info);
}
