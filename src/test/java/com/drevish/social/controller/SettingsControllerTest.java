package com.drevish.social.controller;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.SettingsService;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class SettingsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettingsService settingsService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserInfoService userInfoService;

    private User testUser;
    private UserInfo testUserInfo;

    private ConstraintViolationException violationException;

    @Before
    public void before() {
        testUser = User.builder()
                .email("email@email.com")
                .password("password")
                .build();
        testUserInfo = new UserInfo("name", "surname");

        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoService.getUserInfoByEmail(testUser.getEmail())).thenReturn(testUserInfo);

        ConstraintViolation<String> exampleViolation = mock(ConstraintViolation.class);
        when(exampleViolation.getMessage()).thenReturn("");
        violationException = new ConstraintViolationException(Collections.singleton(exampleViolation));
    }

    @Test
    public void shouldShowSettingsPage() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(content().string(StringContains.containsString(testUser.getEmail())));
    }

    @Test
    public void shouldReturnWithErrorIfPasswordsDontMatch() throws Exception {
        mockMvc.perform(post("/settings")
                .with(csrf())
                .param("changed", "password")
                .param("password", "password")
                .param("passwordNew", "password1")
                .param("passwordNewRepeat", "password2"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldReturnWithErrorIfOldPasswordIncorrect() throws Exception {
        String password = "password";
        doThrow(new InvalidPasswordException("")).when(settingsService)
                .changePassword(testUser, password, password);
        mockMvc.perform(post("/settings")
                .with(csrf())
                .param("changed", "password")
                .param("password", password)
                .param("passwordNew", password)
                .param("passwordNewRepeat", password))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldReturnWithErrorIfNewPasswordInvalid() throws Exception {
        String password = "password";
        doThrow(violationException).when(settingsService).changePassword(testUser, password, password);
        mockMvc.perform(post("/settings")
                .with(csrf())
                .param("changed", "password")
                .param("password", password)
                .param("passwordNew", password)
                .param("passwordNewRepeat", password))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldCallChangePasswordAndNotChangePasswordByItself() throws Exception {
        String oldPassword = testUser.getPassword();
        String newPassword = "newPassword";
        mockMvc.perform(post("/settings")
                .with(csrf())
                .param("changed", "password")
                .param("password", oldPassword)
                .param("passwordNew", newPassword)
                .param("passwordNewRepeat", newPassword))
                .andExpect(redirectedUrl("/settings?success"));
        verify(settingsService, times(1)).changePassword(testUser, oldPassword, newPassword);
        Assert.assertEquals(oldPassword, testUser.getPassword());
    }

    @Test
    public void shouldReturnWithErrorIfEmailIsInvalid() throws Exception {
        String invalidEmail = "invalid";
        doThrow(violationException).when(settingsService).changeEmail(testUser, invalidEmail);
        mockMvc.perform(post("/settings")
                .with(csrf())
                .param("changed", "email")
                .param("email", invalidEmail))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldCallChangeEmailAndNotChangeEmailByItself() throws Exception {
        String emailBefore = testUser.getEmail();
        String newEmail = "newEmail@email.com";
        mockMvc.perform(post("/settings")
                .with(csrf())
                .param("changed", "email")
                .param("email", newEmail))
                .andExpect(redirectedUrl("/settings?success"));
        verify(settingsService, times(1)).changeEmail(testUser, newEmail);
        Assert.assertEquals(emailBefore, testUser.getEmail());
    }
}