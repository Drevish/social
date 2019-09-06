package com.drevish.social.service.impl;

import com.drevish.social.controller.dto.UserInfo;
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
    public void updateInfo(User user, UserInfo info) {
        validateUserInfo(info);
        user.setName(info.getName());
        user.setSurname(info.getSurname());
        userRepository.save(user);
    }

    private void validateUserInfo(UserInfo info) {
        String errorMessage = null;
        if (info.getName() == null || info.getName().length() < 2) {
            errorMessage = "Invalid name";
        }
        if (info.getSurname() == null || info.getSurname().length() < 2) {
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
