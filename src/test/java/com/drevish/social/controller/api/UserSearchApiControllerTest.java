package com.drevish.social.controller.api;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.model.entity.File;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.UserSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {UserSearchApiController.class, CustomAccessDeniedHandler.class})
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class UserSearchApiControllerTest {
    @MockBean
    private UserSearchService userSearchService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnFoundAndMapped() throws Exception {
        UserInfo user1 = new UserInfo("name1", "surname1");
        user1.setId(1L);
        user1.setImage(new File("img1", "", new byte[]{}));
        UserInfo user2 = new UserInfo("name2", "surname2");
        user2.setId(5L);
        user2.setImage(new File("img2", "", new byte[]{}));
        List<UserInfo> allUsers = Arrays.asList(user1, user2);
        when(userSearchService.findAllByNameOrSurname("v")).thenReturn(allUsers);

        mockMvc.perform(get("/api/search/user/{nameOrSurname}", "v"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is(user1.getName())))
                .andExpect(jsonPath("$[0].surname", is(user1.getSurname())))
                .andExpect(jsonPath("$[0].surname", is(user1.getSurname())))
                .andExpect(jsonPath("$[1].id", is(5)))
                .andExpect(jsonPath("$[1].name", is(user2.getName())))
                .andExpect(jsonPath("$[1].surname", is(user2.getSurname())));

        verify(userSearchService, times(1)).findAllByNameOrSurname("v");
        verifyNoMoreInteractions(userSearchService);
    }
}