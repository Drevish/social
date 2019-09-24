package com.drevish.social.service;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.UserInfo;

public interface UserInfoService {
    UserInfo getUserInfoByEmail(String email) throws UserNotFoundException;

    void save(UserInfo userInfo);
}
