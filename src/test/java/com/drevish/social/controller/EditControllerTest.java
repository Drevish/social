package com.drevish.social.controller;

import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.EditService;
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
public class EditControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EditService editService;

    @MockBean
    private UserInfoService userInfoService;

    private User testUser;
    private UserInfo testUserInfo;

    private ConstraintViolationException violationException;

    @Before
    public void before() {
        testUser = User.builder()
                .name("name")
                .surname("surname")
                .email("email@email.com")
                .password("password")
                .build();
        testUserInfo = new UserInfo(testUser.getName(), testUser.getSurname());

        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoService.getUserInfoByEmail(testUser.getEmail())).thenReturn(testUserInfo);

        ConstraintViolation<String> exampleViolation = mock(ConstraintViolation.class);
        when(exampleViolation.getMessage()).thenReturn("");
        violationException = new ConstraintViolationException(Collections.singleton(exampleViolation));
    }

    @Test
    public void shouldShowEditPage() throws Exception {
        mockMvc.perform(get("/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(content().string(StringContains.containsString(testUser.getName())))
                .andExpect(content().string(StringContains.containsString(testUser.getSurname())));
    }

    @Test
    public void shouldReturnWithErrorIfNameIsInvalid() throws Exception {
        UserInfo infoWithInvalidName = new UserInfo("", testUser.getSurname());
        doThrow(violationException).when(editService).updateInfo(testUser, infoWithInvalidName);
        verifyThatErrorIsReturnedBack(infoWithInvalidName);
    }

    @Test
    public void shouldReturnWithErrorIfSurnameIsInvalid() throws Exception {
        UserInfo infoWithInvalidSurname = new UserInfo(testUser.getName(), "");
        doThrow(violationException).when(editService).updateInfo(testUser, infoWithInvalidSurname);
        verifyThatErrorIsReturnedBack(infoWithInvalidSurname);
    }

    private void verifyThatErrorIsReturnedBack(UserInfo userInfo) throws Exception {
        mockMvc.perform(post("/edit")
                .with(csrf())
                .param("name", userInfo.getName())
                .param("surname", userInfo.getSurname()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldCallUpdateAndNotChangeUserByItself() throws Exception {
        String nameBefore = testUser.getName();
        String surnameBefore = testUser.getSurname();
        UserInfo validInfo = new UserInfo("new name", "new surname");
        mockMvc.perform(post("/edit")
                .with(csrf())
                .param("name", validInfo.getName())
                .param("surname", validInfo.getSurname()))
                .andExpect(redirectedUrl("/edit?success"));
        verify(editService, times(1)).updateInfo(testUser, validInfo);
        Assert.assertEquals(nameBefore, testUser.getName());
        Assert.assertEquals(surnameBefore, testUser.getSurname());
    }
}