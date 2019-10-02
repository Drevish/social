package com.drevish.social.service.impl;

import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserInfoRepository;
import com.drevish.social.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSearchServiceImpl implements UserSearchService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public List<UserInfo> findAllByNameOrSurname(String nameOrSurname) {
        return userInfoRepository.findAllByNameStartsWithOrSurnameStartsWith(nameOrSurname, nameOrSurname);
    }
}
