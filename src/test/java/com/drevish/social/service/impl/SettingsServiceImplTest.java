package com.drevish.social.service.impl;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.SettingsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class SettingsServiceImplTest {
    @MockBean
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SettingsService service;

    private String oldPassword = "before";
    private String newPassword = "password";
    private User testUser;

    @Before
    public void before() {
        testUser = new User();
        testUser.setEmail("before");
        testUser.setPassword(passwordEncoder.encode(oldPassword));
    }

    @Test
    @WithMockUser
    public void shouldChangeEmailAndRefreshSecurityContext() {
        String newEmail = "email@gmail.com";
        service.changeEmail(testUser, newEmail);

        assertEquals(newEmail, testUser.getEmail());
        verify(repository, times(1)).save(testUser);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals(newEmail, authentication.getName());
    }

    @Test
    @WithMockUser
    public void shouldChangePassword() {
        service.changePassword(testUser, oldPassword, newPassword);
        assertTrue(passwordEncoder.matches(newPassword, testUser.getPassword()));
        verify(repository, times(1)).save(testUser);
    }

    @Test(expected = InvalidPasswordException.class)
    public void shouldThrowExceptionOnWrongOldPassword() {
        service.changePassword(testUser, "invalid", newPassword);
    }

    @TestConfiguration
    static class SettingsServiceImplTestContextConfiguration {
        @Bean
        public SettingsService settingsService() {
            return new SettingsServiceImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new FakePasswordEncoder();
        }
    }
}