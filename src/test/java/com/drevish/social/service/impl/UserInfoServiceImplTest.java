package com.drevish.social.service.impl;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserInfoRepository;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserInfoServiceImplTest {
    @MockBean
    private UserInfoRepository userInfoRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    private User testUser;
    private UserInfo testUserInfo;

    @Before
    public void before() {
        testUser = User.builder()
                .id(1L)
                .email("email")
                .password("password")
                .build();

        testUserInfo = new UserInfo(testUser.getId(), testUser, "name", "surname");
    }

    @Test
    public void shouldReturnUserInfoByEmail() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.of(testUserInfo));
        UserInfo userInfo = userInfoService.getUserInfoByEmail(testUser.getEmail());
        assertEquals(testUserInfo, userInfo);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFoundInUserService() {
        when(userService.getUserByEmail(testUser.getEmail())).thenThrow(UserNotFoundException.class);
        userInfoService.getUserInfoByEmail(testUser.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFoundInUserInfoRepository() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.empty());
        userInfoService.getUserInfoByEmail(testUser.getEmail());
    }

    @Test
    public void shouldSaveUserInfo() {
        userInfoService.save(testUserInfo);
        verify(userInfoRepository, times(1)).save(testUserInfo);
    }

    @Test
    public void shouldSetIdAndUserAndSaveUserInfo() {
        UserInfo userInfoWithOnlyNameAndSurname = new UserInfo(testUserInfo.getName(), testUserInfo.getSurname());
        userInfoService.saveForUser(userInfoWithOnlyNameAndSurname, testUser);
        verify(userInfoRepository, times(1)).save(testUserInfo);
    }

    @TestConfiguration
    static class UserInfoServiceImplTestContextConfiguration {
        @Bean
        public UserInfoService userInfoService() {
            return new UserInfoServiceImpl();
        }
    }
}