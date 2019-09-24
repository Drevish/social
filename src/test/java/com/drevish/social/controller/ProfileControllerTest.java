package com.drevish.social.controller;

import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.hamcrest.core.StringContains;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserInfoService userInfoService;

    private User testUser;
    private UserInfo testUserInfo;

    @Before
    public void before() {
        testUser = User.builder()
                .name("name")
                .surname("surname")
                .email("email")
                .build();
        testUserInfo = new UserInfo(testUser.getName(), testUser.getSurname());

        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoService.getUserInfoByEmail(testUser.getEmail())).thenReturn(testUserInfo);
    }

    @Test
    public void shouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "email")
    public void shouldShowProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", testUser))
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(content().string(StringContains.containsString(testUser.getName())))
                .andExpect(content().string(StringContains.containsString(testUser.getSurname())));
    }
}