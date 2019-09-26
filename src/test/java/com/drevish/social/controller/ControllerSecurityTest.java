package com.drevish.social.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerSecurityTest extends ControllerTestWithUserAndUserInfo {

    @Autowired
    private MockMvc mockMvc;

    private List<String> requestUrlsForUser;

    @Before
    public void before() {
        requestUrlsForUser = Arrays.asList("/profile", "/edit", "/settings");
    }

    @Test
    @WithMockUser(username = "email@email.com")
    public void shouldShowPage() throws Exception {
        for (String requestUrl : requestUrlsForUser) {
            verifyThatShowsPage(requestUrl);
        }
    }

    @Test
    @WithAnonymousUser
    public void shouldRedirectToLoginPage() throws Exception {
        for (String requestUrl : requestUrlsForUser) {
            verifyThatRedirectsToLoginPage(requestUrl);
        }
    }

    private void verifyThatRedirectsToLoginPage(String requestUrl) throws Exception {
        mockMvc.perform(get(requestUrl))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    private void verifyThatShowsPage(String requestUrl) throws Exception {
        mockMvc.perform(get(requestUrl))
                .andExpect(status().isOk());
    }
}
