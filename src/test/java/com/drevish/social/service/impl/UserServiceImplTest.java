package com.drevish.social.service.impl;

import com.drevish.social.controller.dto.UserRegistrationInfo;
import com.drevish.social.exception.UserExistsException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @MockBean
    private UserRepository repository;

    @MockBean
    private UserInfoService userInfoService;

    @Autowired
    private UserService service;

    private UserRegistrationInfo registrationInfo;
    private String existingEmail;

    @Before
    public void before() {
        existingEmail = "email";

        when(repository.findByEmail(existingEmail)).thenReturn(Optional.of(new User()));

        registrationInfo = new UserRegistrationInfo();
        registrationInfo.setEmail("new");
        registrationInfo.setPassword("new");
        registrationInfo.setPasswordCheck("new");
        registrationInfo.setName("name");
        registrationInfo.setSurname("surname");
    }

    @Test
    public void shouldGetByEmail() {
        assertNotNull(service.getUserByEmail("email"));
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionByEmail() {
        service.getUserByEmail("invalid");
    }

    @Test
    public void shouldGetById() {
        when(repository.findById(0L)).thenReturn(Optional.of(new User()));
        assertNotNull(service.getUserById(0L));
    }

    @Test
    public void shouldSaveUserAndUserInfoWhenRegister() {
        User expectedUser = User.builder()
                .email(registrationInfo.getEmail())
                .password(new FakePasswordEncoder().encode(registrationInfo.getPassword()))
                .build();
        UserInfo expectedInfo = new UserInfo(registrationInfo.getName(), registrationInfo.getSurname());
        service.register(registrationInfo);
        verify(repository, times(1)).save(expectedUser);
        verify(userInfoService, times(1)).saveForUser(expectedInfo, expectedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowUserNotFoundExceptionById() {
        when(repository.findById(-1L)).thenReturn(Optional.empty());
        service.getUserById(-1L);
    }

    @Test(expected = UserExistsException.class)
    public void shouldThrowUserExistsExceptionWhenRegister() {
        registrationInfo.setEmail(existingEmail);
        service.register(registrationInfo);
    }

    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new FakePasswordEncoder();
        }
    }

    private static class FakePasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence charSequence) {
            return "encoded" + charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return charSequence.toString().equals("encoded" + s);
        }
    }
}