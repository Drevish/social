package com.drevish.social.service.impl;

import com.drevish.social.exception.UserExistsException;
import com.drevish.social.exception.UserNotFoundException;
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

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("No user with email " + email + " exists")
    );
  }

  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("No user with id " + id + " exists")
    );
  }

  public void register(User user) {
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new UserExistsException("User with email " + user.getEmail() + " already exists");
    }

    user.setPassword(encryptPassword(user.getPassword()));
    userRepository.save(user);
  }

  private String encryptPassword(String password) {
    return new BCryptPasswordEncoder().encode(password);
  }
}
