package com.drevish.social.model.repository;

import com.drevish.social.model.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {
  @Autowired
  private UserRepository repository;

  @Test(expected = DataIntegrityViolationException.class)
  public void shouldNotSaveExistingUserAsNew() {
    User user = new User();
    user.setEmail("email");
    user.setPassword("password");
    repository.save(user);

    User user2 = new User();
    user2.setEmail("email");
    user2.setPassword("password");
    repository.save(user2);
  }

  @Test
  public void shouldSaveNewUser() {
    User user = new User();
    user.setEmail("email");
    user.setPassword("password");
    repository.save(user);
  }

  @Test
  public void shouldGetUserById() {
    User user = new User();
    user.setEmail("email");
    user.setPassword("password");
    long saved_id = repository.save(user).getId();
    User saved = repository.findById(saved_id).get();
    assertEquals(user, saved);
  }

  @Test
  public void shouldGetUserByEmail() {
    User user = new User();
    user.setEmail("email");
    user.setPassword("password");
    repository.save(user);
    User saved = repository.findByEmail("email").get();
    assertEquals(user, saved);
  }

  @Test
  public void shouldGetEmptyUser() {
    Optional<User> userById = repository.findById(0L);
    Optional<User> userByEmail = repository.findByEmail("email");
    assertFalse(userById.isPresent());
    assertFalse(userByEmail.isPresent());
  }
}