package com.drevish.social.service.impl;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.exception.UserValidationException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.EditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditServiceImpl implements EditService {
    @Autowired
    private UserRepository userRepository;

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
}
