package com.drevish.social.controller;

import com.drevish.social.model.entity.User;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private User testUser;

    @Before
    public void before() {
        testUser = User.builder()
                .name("name")
                .surname("surname")
                .email("email")
                .build();
    }

    @Test
    public void shouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldShowProfilePage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.putValue("user", testUser);
        mockMvc.perform(get("/profile")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(StringContains.containsString(testUser.getName())))
                .andExpect(content().string(StringContains.containsString(testUser.getSurname())));
    }
}