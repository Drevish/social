package com.drevish.social.service;

import com.drevish.social.exception.UserExistsException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(SpringRunner.class)
public class UserServiceTest {

  @MockBean
  private UserRepository repository;

  @Autowired
  private UserService service;

  @Before
  public void before() {
    when(repository.findByEmail("email")).thenReturn(Optional.of(new User()));
    when(repository.findById(0L)).thenReturn(Optional.of(new User()));
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
    assertNotNull(service.getUserById(0L));
  }

  @Test(expected = UserNotFoundException.class)
  public void shouldThrowUserNotFoundExceptionById() {
    service.getUserById(-1L);
  }

  @Test
  public void shouldSave() {
    User user = new User();
    user.setEmail("new");
    user.setPassword("new");
    service.register(user);
    verify(repository, times(1)).save(user);
  }

  @Test(expected = UserExistsException.class)
  public void shouldThrowUserExistsException() {
    User user = new User();
    user.setEmail("email");
    user.setPassword("new");
    service.register(user);
  }

  @TestConfiguration
  static class UserServiceTestContextConfiguration {
    @Bean
    public UserService userService() {
      return new UserService();
    }
  }
}