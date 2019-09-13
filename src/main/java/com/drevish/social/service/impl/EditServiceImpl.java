package com.drevish.social.service.impl;

import com.drevish.social.controller.dto.UserInfo;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.EditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class EditServiceImpl implements EditService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void updateInfo(User user, UserInfo info) {
        user.setName(info.getName());
        user.setSurname(info.getSurname());
        userRepository.save(user);
    }
}
