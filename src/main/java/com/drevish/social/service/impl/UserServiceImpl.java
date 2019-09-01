package com.drevish.social.service.impl;

import com.drevish.social.exception.UserExistsException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.exception.UserValidationException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("No user with email " + email + " exists")
    );
  }

  @Override
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("No user with id " + id + " exists")
    );
  }

  @Override
  public void register(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new UserExistsException("User with email " + user.getEmail() + " already exists");
    }

    user.setPassword(encryptPassword(user.getPassword()));
    userRepository.save(user);
  }

  @Override
  public void updateInfo(User user) {
    validateUserInfo(user);
    userRepository.save(user);
  }

  private void validateUserInfo(User user) {
    String errorMessage = null;
    if (user.getName() == null || user.getName().length() < 2) {
      errorMessage = "Invalid name";
    }
    if (user.getSurname() == null || user.getSurname().length() < 2) {
      errorMessage = "Invalid surname";
    }
    if (errorMessage != null) {
      throw new UserValidationException(errorMessage);
    }
  }

  private String encryptPassword(String password) {
    return new BCryptPasswordEncoder().encode(password);
  }
}
