package com.drevish.social.controller;

import com.drevish.social.model.entity.UserInfo;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.drevish.social.controller.ControllerTestUtils.testUser;
import static com.drevish.social.controller.ControllerTestUtils.testUserInfo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = EditController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class EditControllerTest extends ControllerTestWithUserAndUserInfo {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldShowEditPage() throws Exception {
        mockMvc.perform(get("/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(content().string(StringContains.containsString(testUserInfo.getName())))
                .andExpect(content().string(StringContains.containsString(testUserInfo.getSurname())));
    }

    @Test
    public void shouldCallUpdateAndNotChangeUserInfoByItself() throws Exception {
        UserInfo beforePost = new UserInfo(testUserInfo.getName(), testUserInfo.getSurname());
        UserInfo newUserInfo = new UserInfo("new name", "new surname");
        postUserInfoToEdit(newUserInfo)
                .andExpect(redirectedUrl("/edit?success"));

        verify(userInfoService, times(1)).saveForUser(newUserInfo, testUser);
        Assert.assertEquals(testUserInfo.getName(), beforePost.getName());
        Assert.assertEquals(testUserInfo.getSurname(), beforePost.getSurname());
    }

    @Test
    public void shouldReturnWithErrorIfNameIsInvalid() throws Exception {
        UserInfo infoWithInvalidName = new UserInfo("", testUserInfo.getSurname());
        verifyThatReturnedBackWithErrorAttribute(infoWithInvalidName);
    }

    @Test
    public void shouldReturnWithErrorIfSurnameIsInvalid() throws Exception {
        UserInfo infoWithInvalidSurname = new UserInfo(testUserInfo.getName(), "");
        verifyThatReturnedBackWithErrorAttribute(infoWithInvalidSurname);
    }

    private void verifyThatReturnedBackWithErrorAttribute(UserInfo userInfo) throws Exception {
        postUserInfoToEdit(userInfo)
                .andExpect(status().isOk())
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attributeExists("error"));
    }

    private ResultActions postUserInfoToEdit(UserInfo userInfo) throws Exception {
        return mockMvc.perform(post("/edit")
                .with(csrf())
                .param("name", userInfo.getName())
                .param("surname", userInfo.getSurname()));
    }
}