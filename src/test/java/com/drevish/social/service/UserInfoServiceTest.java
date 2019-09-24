package com.drevish.social.service;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserInfoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoServiceTest {
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
                .name("name")
                .surname("surname")
                .email("email")
                .password("password")
                .build();

        testUserInfo = new UserInfo(testUser.getId(), testUser, testUser.getName(), testUser.getSurname());
    }


    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFoundInUserService() {
        when(userService.getUserByEmail(testUser.getEmail())).thenThrow(UserNotFoundException.class);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.of(testUserInfo));
        userInfoService.getUserInfoByEmail(testUser.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFoundInUserInfoRepository() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.empty());
        userInfoService.getUserInfoByEmail(testUser.getEmail());
    }

    @Test
    public void shouldReturnUserInfoByEmail() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.of(testUserInfo));
        UserInfo userInfo = userInfoService.getUserInfoByEmail(testUser.getEmail());
        assertEquals(testUserInfo, userInfo);
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
}