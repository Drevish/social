package com.drevish.social.controller;

import com.drevish.social.controller.dto.UserRegistrationInfo;
import com.drevish.social.exception.UserExistsException;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User testUser;
    private UserRegistrationInfo testRegistrationInfo;

    private LinkedMultiValueMap<String, String> paramsMap;

    @Before
    public void before() {
        testUser = User.builder()

                .email("email@email.com")
                .password("password")
                .build();

        testRegistrationInfo = new UserRegistrationInfo();
        testRegistrationInfo.setEmail(testUser.getEmail());
        testRegistrationInfo.setPassword(testUser.getPassword());
        testRegistrationInfo.setPasswordCheck(testUser.getPassword());
        testRegistrationInfo.setName("name");
        testRegistrationInfo.setSurname("surname");

        paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("name", "name");
        paramsMap.add("surname", "surname");
        paramsMap.add("email", testUser.getEmail());
        paramsMap.add("password", testUser.getPassword());
        paramsMap.add("passwordCheck", testUser.getPassword());
    }

    @Test
    public void shouldShowRegistrationPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnRegistrationPageIfNameIsInvalid() throws Exception {
        reassignValueToParamsMap("name", "");
        mockMvc.perform(post("/register")
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("userRegistrationInfo", "name"));
    }

    @Test
    public void shouldReturnRegistrationPageIfSurnameIsInvalid() throws Exception {
        reassignValueToParamsMap("surname", "");
        mockMvc.perform(post("/register")
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("userRegistrationInfo", "surname"));
    }

    @Test
    public void shouldReturnRegistrationPageIfEmailIsInvalid() throws Exception {
        reassignValueToParamsMap("email", "");
        mockMvc.perform(post("/register")
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("userRegistrationInfo", "email"));
    }

    @Test
    public void shouldReturnRegistrationPageIfPasswordsIsInvalid() throws Exception {
        reassignValueToParamsMap("password", "");
        mockMvc.perform(post("/register")
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("userRegistrationInfo", "password"));
    }

    @Test
    public void shouldReturnRegistrationPageIfPasswordsDontMatch() throws Exception {
        reassignValueToParamsMap("passwordCheck", "");
        mockMvc.perform(post("/register")
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldReturnRegistrationPageIfUserAlreadyExists() throws Exception {
        doThrow(new UserExistsException("")).when(userService).register(testRegistrationInfo);
        mockMvc.perform(post("/register")
                .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void shouldRegisterAndRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/register")
                .params(paramsMap))
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(testRegistrationInfo);
    }

    private void reassignValueToParamsMap(String name, String value) {
        paramsMap.remove(name);
        paramsMap.add(name, value);
    }
}