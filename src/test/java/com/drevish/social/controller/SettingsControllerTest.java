package com.drevish.social.controller;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.service.SettingsService;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import static com.drevish.social.controller.ControllerTestUtils.testUser;
import static com.drevish.social.controller.ControllerTestUtils.testUserInfo;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class SettingsControllerTest extends ControllerTestWithUserAndUserInfo {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettingsService settingsService;

    private LinkedMultiValueMap<String, String> paramsMapForChangePasswordRequest;
    private String oldPassword = testUser.getPassword();
    private String newPassword = "newPassword";

    @Before
    public void before() {
        paramsMapForChangePasswordRequest = new LinkedMultiValueMap<>();
        paramsMapForChangePasswordRequest.set("changed", "password");
        paramsMapForChangePasswordRequest.set("passwordOld", oldPassword);
        paramsMapForChangePasswordRequest.set("password", newPassword);
        paramsMapForChangePasswordRequest.set("passwordNewRepeat", newPassword);
    }

    @Test
    public void shouldShowSettingsPage() throws Exception {
        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(content().string(StringContains.containsString(testUser.getEmail())));
    }

    @Test
    public void shouldCallChangePasswordAndNotChangePasswordByItself() throws Exception {
        postChangePasswordToSettings()
                .andExpect(redirectedUrl("/settings?success"));
        verify(settingsService, times(1)).changePassword(testUser, oldPassword, newPassword);
        Assert.assertEquals(oldPassword, testUser.getPassword());
    }

    @Test
    public void shouldReturnWithErrorIfPasswordsDontMatch() throws Exception {
        paramsMapForChangePasswordRequest.remove("passwordNewRepeat");
        paramsMapForChangePasswordRequest.set("passwordNewRepeat", "another");
        verifyThatReturnedBackWithErrorAttribute(postChangePasswordToSettings());
    }

    @Test
    public void shouldReturnWithErrorIfOldPasswordIncorrect() throws Exception {
        doThrow(new InvalidPasswordException("")).when(settingsService)
                .changePassword(testUser, oldPassword, newPassword);
        verifyThatReturnedBackWithErrorAttribute(postChangePasswordToSettings());
    }

    @Test
    public void shouldReturnWithErrorIfNewPasswordIsNull() throws Exception {
        paramsMapForChangePasswordRequest.remove("password");
        verifyThatReturnedBackWithErrorAttribute(postChangePasswordToSettings());
    }

    @Test
    public void shouldReturnWithErrorIfNewPasswordIsTooShort() throws Exception {
        paramsMapForChangePasswordRequest.remove("password");
        paramsMapForChangePasswordRequest.set("password", "q");
        verifyThatReturnedBackWithErrorAttribute(postChangePasswordToSettings());
    }

    @Test
    public void shouldReturnWithErrorIfNewPasswordDoesNotMatchPattern() throws Exception {
        paramsMapForChangePasswordRequest.remove("password");
        paramsMapForChangePasswordRequest.set("password", "пароль");
        verifyThatReturnedBackWithErrorAttribute(postChangePasswordToSettings());
    }

    private ResultActions postChangePasswordToSettings() throws Exception {
        return mockMvc.perform(post("/settings")
                .with(csrf())
                .params(paramsMapForChangePasswordRequest));
    }

    private ResultActions verifyThatReturnedBackWithErrorAttribute(ResultActions action) throws Exception {
        return action.andExpect(status().isOk())
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldReturnWithErrorIfEmailIsNull() throws Exception {
        verifyThatReturnedBackWithErrorAttribute(postChangeEmailToSettings(null));
    }

    @Test
    public void shouldReturnWithErrorIfEmailIsInvalid() throws Exception {
        verifyThatReturnedBackWithErrorAttribute(postChangeEmailToSettings("invalid"));
    }

    @Test
    public void shouldCallChangeEmailAndNotChangeEmailByItself() throws Exception {
        String emailBefore = testUser.getEmail();
        String newEmail = "newEmail@email.com";
        postChangeEmailToSettings(newEmail)
                .andExpect(redirectedUrl("/settings?success"));

        verify(settingsService, times(1)).changeEmail(testUser, newEmail);
        Assert.assertEquals(emailBefore, testUser.getEmail());
    }

    private ResultActions postChangeEmailToSettings(String invalidEmail) throws Exception {
        return mockMvc.perform(post("/settings")
                .with(csrf())
                .param("changed", "email")
                .param("email", invalidEmail));
    }
}