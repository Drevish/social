package com.drevish.social.controller;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drevish.social.controller.ControllerTestUtils.testUser;
import static com.drevish.social.controller.ControllerTestUtils.testUserInfo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class ProfileControllerTest extends ControllerTestWithUserAndUserInfo {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldShowOwnProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userInfo", testUserInfo))
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
                .andExpect(model().attribute("userInfo", anotherUserInfo))
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
}