package com.drevish.social.service.impl;

import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserInfoRepository;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserService userService;

    @Override
    public UserInfo getUserInfoByEmail(String email) {
        User user = userService.getUserByEmail(email);
        return userInfoRepository.findById(user.getId()).orElseThrow(() ->
                new UserNotFoundException("No user info with specified user id was found"));
    }
}