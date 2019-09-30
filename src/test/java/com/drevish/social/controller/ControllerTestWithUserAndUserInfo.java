package com.drevish.social.controller;

import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.junit.Before;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.drevish.social.TestUtils.testUser;
import static com.drevish.social.TestUtils.testUserInfo;
import static org.mockito.Mockito.when;

public abstract class ControllerTestWithUserAndUserInfo {
    @MockBean
    protected UserService userService;

    @MockBean
    protected UserInfoService userInfoService;

    @Before
    public void mockUserServices() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoService.getUserInfoByEmail(testUser.getEmail())).thenReturn(testUserInfo);
    }
}
