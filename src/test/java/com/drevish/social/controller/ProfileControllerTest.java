package com.drevish.social.controller;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.drevish.social.TestUtils.testUser;
import static com.drevish.social.TestUtils.testUserInfo;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProfileController.class, CustomAccessDeniedHandler.class})
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class ProfileControllerTest extends ControllerTestWithUserAndUserInfo {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldShowOwnProfilePage() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/profile"))
                .andExpect(status().isOk());
        verifyProfilePageModelAttributes(resultActions);
    }

    private ResultActions verifyProfilePageModelAttributes(ResultActions actions) throws Exception {
        return actions.andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(content().string(StringContains.containsString(testUserInfo.getName())))
                .andExpect(content().string(StringContains.containsString(testUserInfo.getSurname())))
                .andExpect(content().string(StringContains.containsString(testUser.getEmail())));
    }

    @Test
    public void shouldShowAnotherUserPage() throws Exception {
        User anotherUser = User.builder()
                .id(2L)
                .email("another@gmail.com")
                .build();
        UserInfo anotherUserInfo = new UserInfo("anotherName", "anotherSurname");
        when(userService.getUserById(2L)).thenReturn(anotherUser);
        when(userInfoService.getUserInfoByEmail("another@gmail.com")).thenReturn(anotherUserInfo);

        mockMvc.perform(get("/profile/id{id}", anotherUser.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attribute("otherUserInfo", anotherUserInfo))
                .andExpect(content().string(StringContains.containsString(anotherUserInfo.getName())))
                .andExpect(content().string(StringContains.containsString(anotherUserInfo.getSurname())));
    }

    @Test
    public void shouldRedirectToProfilePageIfIdIsTheCurrentUserId() throws Exception {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        mockMvc.perform(get("/profile/id{id}", testUser.getId()))
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void shouldRedirectToProfilePageIfInvalidUserId() throws Exception {
        when(userService.getUserById(0L)).thenThrow(new UserNotFoundException(""));
        mockMvc.perform(get("/profile/id{id}", 0L))
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void shouldUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.png", "image/PNG", "data".getBytes());
        ResultActions actions = mockMvc.perform(multipart("/profile")
                .file(file)
                .with(csrf())
                .param("change", "upload-image"))
                .andExpect(status().isOk());
        verifyProfilePageModelAttributes(actions);
        verify(userInfoService, times(1)).setImage(file, testUserInfo);
    }


    @Test
    public void shouldDeleteImage() throws Exception {
        ResultActions actions = mockMvc.perform(post("/profile")
                .with(csrf())
                .param("change", "delete-image"))
                .andExpect(status().isOk());
        verifyProfilePageModelAttributes(actions);
        verify(userInfoService, times(1)).deleteImage(testUserInfo);
    }
}