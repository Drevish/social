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

    @Override
    public void save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    @Override
    public void saveForUser(UserInfo userInfo, User user) {
        userInfo.setId(user.getId());
        userInfo.setUser(user);
        save(userInfo);
    }
}
