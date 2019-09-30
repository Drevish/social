package com.drevish.social.service.impl;

import com.drevish.social.controller.dto.UserRegistrationInfo;
import com.drevish.social.exception.UserExistsException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoService infoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public void register(UserRegistrationInfo registrationInfo) {
        if (userRepository.findByEmail(registrationInfo.getEmail()).isPresent()) {
            throw new UserExistsException("User with email " + registrationInfo.getEmail() + " already exists");
        }

        User user = assembleUserFromRegistrationInfo(registrationInfo);
        userRepository.save(user);

        UserInfo userInfo = new UserInfo(registrationInfo.getName(), registrationInfo.getSurname());
        userInfo.setId(user.getId());
        userInfo.setUser(user);
        infoService.save(userInfo);
    }

    private User assembleUserFromRegistrationInfo(UserRegistrationInfo info) {
        return User.builder()
                .email(info.getEmail())
                .password(passwordEncoder.encode(info.getPassword()))
                .build();
    }
}
