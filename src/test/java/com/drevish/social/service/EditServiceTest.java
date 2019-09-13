package com.drevish.social.service;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EditServiceTest {
    @MockBean
    private UserRepository repository;

    @Autowired
    private EditService service;

    private String name = "name";
    private String surname = "surname";
    private UserInfo userInfo;

    @Before
    public void before() {
        userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setSurname(surname);
    }

    @Test
    public void shouldUpdateInfo() {
        User user = new User();
        service.updateInfo(user, userInfo);
        verify(repository, times(1)).save(user);
        assertEquals(name, user.getName());
        assertEquals(surname, user.getSurname());
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnNullName() {
        userInfo.setName(null);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserInfo>> violations = validator.validate(userInfo);
        System.out.println(violations);
        service.updateInfo(new User(), userInfo);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnTooShortName() {
        userInfo.setName("q");
        service.updateInfo(new User(), userInfo);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnNullSurname() {
        userInfo.setSurname(null);
        service.updateInfo(new User(), userInfo);
    }

    @Test(expected = ConstraintViolationException.class)
    public void shouldThrowValidationExceptionOnTooShortSurname() {
        userInfo.setSurname("q");
        service.updateInfo(new User(), userInfo);
    }
}