package com.drevish.social.controller;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.EditService;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser
public class EditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EditService editService;

    private User testUser;

    private MockHttpSession httpSessionWithUser;

    private ConstraintViolationException violationException;

    @Before
    public void before() {
        testUser = User.builder()
                .name("name")
                .surname("surname")
                .email("email@email.com")
                .password("password")
                .build();

        httpSessionWithUser = new MockHttpSession();
        httpSessionWithUser.putValue("user", testUser);

        ConstraintViolation<String> exampleViolation = mock(ConstraintViolation.class);
        when(exampleViolation.getMessage()).thenReturn("");
        violationException = new ConstraintViolationException(Collections.singleton(exampleViolation));
    }

    @Test
    public void shouldShowEditPage() throws Exception {
        mockMvc.perform(get("/edit")
                .session(httpSessionWithUser))
                .andExpect(status().isOk())
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
                .session(httpSessionWithUser)
                .param("name", userInfo.getName())
                .param("surname", userInfo.getSurname()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldCallUpdateAndNotChangeUserByItself() throws Exception {
        String nameBefore = testUser.getName();
        String surnameBefore = testUser.getSurname();
        UserInfo validInfo = new UserInfo("new name", "new surname");
        mockMvc.perform(post("/edit")
                .param("name", validInfo.getName())
                .param("surname", validInfo.getSurname())
                .session(httpSessionWithUser))
                .andExpect(redirectedUrl("/edit?success"));
        verify(editService, times(1)).updateInfo(testUser, validInfo);
        Assert.assertEquals(nameBefore, testUser.getName());
        Assert.assertEquals(surnameBefore, testUser.getSurname());
    }
}