package com.drevish.social.controller;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.controller.dto.UserSearchDto;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.UserSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.drevish.social.TestUtils.testUserInfo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserSearchController.class, CustomAccessDeniedHandler.class})
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class UserSearchControllerTest extends ControllerTestWithUserAndUserInfo {
    @MockBean
    private UserSearchService userSearchService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnFoundAndMapped() throws Exception {
        UserInfo user1 = new UserInfo("name1", "surname1");
        user1.setId(1L);
        UserInfo user2 = new UserInfo("name2", "surname2");
        user2.setId(5L);
        List<UserInfo> allUsers = Arrays.asList(user1, user2);
        when(userSearchService.findAllByNameOrSurname("v")).thenReturn(allUsers);

        List<UserSearchDto> users = allUsers.stream().map(UserSearchDto::assemble).collect(Collectors.toList());

        mockMvc.perform(get("/search/user/?q={nameAndSurname}", "v"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", users))
                .andExpect(model().attribute("userInfo", testUserInfo));

        verify(userSearchService, times(1)).findAllByNameOrSurname("v");
        verifyNoMoreInteractions(userSearchService);
    }
}