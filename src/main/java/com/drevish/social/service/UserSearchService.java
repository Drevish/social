package com.drevish.social.service;

import com.drevish.social.model.entity.UserInfo;

import java.util.List;

public interface UserSearchService {
    List<UserInfo> findAllByNameOrSurname(String nameOrSurname);
}
