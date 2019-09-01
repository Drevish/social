package com.drevish.social.service;

import com.drevish.social.exception.UserExistsException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;

public interface UserService {
  User getUserByEmail(String email) throws UserNotFoundException;

  User getUserById(Long id) throws UserNotFoundException;

  void register(User user) throws UserExistsException;
}
