package com.drevish.social.service;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.exception.UserValidationException;
import com.drevish.social.model.entity.User;

public interface EditService {
    void updateInfo(User user, UserInfo info) throws UserValidationException;
}
