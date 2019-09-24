package com.drevish.social.service;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SettingsServiceTest {
    @MockBean
    private UserRepository repository;

    @Autowired
    private SettingsService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String oldPassword = "before";
    private String validPassword = "password";
    private User user;

    @Before
    public void before() {
        user = new User();
        user.setEmail("before");
        user.setPassword(passwordEncoder.encode(oldPassword));
    }

    @Test
    @WithMockUser
    public void shouldChangeEmail() {
        String email = "email@gmail.com";
        service.changeEmail(user, email);
        assertEquals(email, user.getEmail());
        verify(repository, times(1)).save(user);
    }

    @Test
    @WithMockUser
    public void shouldRefreshSecurityContextWhenChangedEmail() {
        String email = "email@gmail.com";
        service.changeEmail(user, email);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals(email, authentication.getName());
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnNullEmail() {
        service.changeEmail(user, null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnInvalidEmail() {
        service.changeEmail(user, "email");
    }

    @Test
    @WithMockUser
    public void shouldChangePassword() {
        service.changePassword(user, oldPassword, validPassword);
        assertTrue(passwordEncoder.matches(validPassword, user.getPassword()));
        verify(repository, times(1)).save(user);
    }

    @Test(expected = InvalidPasswordException.class)
    public void shouldThrowExceptionOnWrongOldPassword() {
        service.changePassword(user, "invalid", validPassword);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnNullPassword() {
        service.changePassword(user, oldPassword, null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnTooShortPassword() {
        service.changePassword(user, oldPassword, "q");
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnNonLatinPassword() {
        service.changePassword(user, oldPassword, "пароль");
    }
}