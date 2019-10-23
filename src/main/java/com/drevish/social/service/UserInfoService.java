package com.drevish.social.service;

import com.drevish.social.controller.dto.UserInfoDto;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UserInfoService {
    UserInfo getUserInfoByEmail(String email) throws UserNotFoundException;

    UserInfo getUserInfoById(Long id) throws UserNotFoundException;

    void save(UserInfo userInfo);

    void updateNameAndSurname(UserInfoDto newValues, User user);

    void setImage(MultipartFile image, UserInfo userInfo);

    void deleteImage(UserInfo userInfo);
}
