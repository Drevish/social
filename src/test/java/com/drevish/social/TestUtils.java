package com.drevish.social;

import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;

import java.util.ArrayList;

public interface TestUtils {
    User testUser = User.builder()
            .id(1L)
            .email("email@email.com")
            .password("password")
            .roles(new ArrayList<>())
            .build();

    UserInfo testUserInfo = new UserInfo(1L, testUser, "name", "surname");
}
