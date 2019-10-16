package com.drevish.social.controller;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.controller.dto.UserRegistrationInfo;
import com.drevish.social.exception.UserExistsException;
import com.drevish.social.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import static com.drevish.social.TestUtils.testUser;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {RegistrationController.class, CustomAccessDeniedHandler.class})
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserRegistrationInfo testRegistrationInfo;

    private LinkedMultiValueMap<String, String> paramsMapWithRegistrationInfo;

    @Before
    public void before() {
        testRegistrationInfo = new UserRegistrationInfo();
        testRegistrationInfo.setEmail(testUser.getEmail());
        testRegistrationInfo.setPassword(testUser.getPassword());
        testRegistrationInfo.setPasswordCheck(testUser.getPassword());
        testRegistrationInfo.setName("name");
        testRegistrationInfo.setSurname("surname");

        paramsMapWithRegistrationInfo = new LinkedMultiValueMap<>();
        paramsMapWithRegistrationInfo.add("name", testRegistrationInfo.getName());
        paramsMapWithRegistrationInfo.add("surname", testRegistrationInfo.getSurname());
        paramsMapWithRegistrationInfo.add("email", testUser.getEmail());
        paramsMapWithRegistrationInfo.add("password", testUser.getPassword());
        paramsMapWithRegistrationInfo.add("passwordCheck", testUser.getPassword());
    }

    @Test
    public void shouldShowRegistrationPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRegisterAndRedirectToLoginPage() throws Exception {
        postRegistrationInfoToRegister()
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(testRegistrationInfo);
    }

    @Test
    public void shouldReturnBackIfNameIsInvalid() throws Exception {
        verifyThatReturnsBackWithFieldErrorAttribute("name");
    }


    @Test
    public void shouldReturnBackIfSurnameIsInvalid() throws Exception {
        verifyThatReturnsBackWithFieldErrorAttribute("surname");
    }

    @Test
    public void shouldReturnBackIfEmailIsInvalid() throws Exception {
        verifyThatReturnsBackWithFieldErrorAttribute("email");
    }

    @Test
    public void shouldReturnBackIfPasswordsIsInvalid() throws Exception {
        verifyThatReturnsBackWithFieldErrorAttribute("password");
    }

    private void verifyThatReturnsBackWithFieldErrorAttribute(String invalidField) throws Exception {
        paramsMapWithRegistrationInfo.remove(invalidField);
        postRegistrationInfoToRegister()
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("userRegistrationInfo", invalidField));
        verify(userService, never()).register(testRegistrationInfo);
    }

    @Test
    public void shouldReturnRegistrationPageIfPasswordsDontMatch() throws Exception {
        paramsMapWithRegistrationInfo.remove("passwordCheck");
        verifyThatReturnsBackWithErrorAttribute();
    }

    @Test
    public void shouldReturnRegistrationPageIfUserAlreadyExists() throws Exception {
        doThrow(new UserExistsException("")).when(userService).register(testRegistrationInfo);
        verifyThatReturnsBackWithErrorAttribute();
    }

    private void verifyThatReturnsBackWithErrorAttribute() throws Exception {
        postRegistrationInfoToRegister()
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }

    private ResultActions postRegistrationInfoToRegister() throws Exception {
        return mockMvc.perform(post("/register")
                .with(csrf())
                .params(paramsMapWithRegistrationInfo));
    }
}