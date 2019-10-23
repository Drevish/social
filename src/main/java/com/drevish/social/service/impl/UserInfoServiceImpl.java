package com.drevish.social.service.impl;

import com.drevish.social.controller.dto.UserInfoDto;
import com.drevish.social.exception.InvalidImageException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.File;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserInfoRepository;
import com.drevish.social.service.FileStorageService;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public UserInfo getUserInfoByEmail(String email) {
        User user = userService.getUserByEmail(email);
        return getUserInfoByUser(user);
    }

    @Override
    public UserInfo getUserInfoById(Long id) throws UserNotFoundException {
        return getUserInfoByUser(User.builder().id(id).build());
    }

    private UserInfo getUserInfoByUser(User user) {
        return userInfoRepository.findById(user.getId()).orElseThrow(() ->
                new UserNotFoundException("No user info with specified user id was found"));
    }

    @Override
    public void save(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    @Override
    public void updateNameAndSurname(UserInfoDto newValues, User user) {
        UserInfo originalInfo = getUserInfoByUser(user);
        originalInfo.setName(newValues.getName());
        originalInfo.setSurname(newValues.getSurname());
        save(originalInfo);
    }

    @Override
    public void setImage(MultipartFile image, UserInfo userInfo) {
        if (image.getSize() == 0) {
            throw new InvalidImageException("Empty image or no image at all");
        }
        if (!image.getContentType().startsWith("image")) {
            throw new InvalidImageException("File is not an image");
        }

        //delete old image if present
        deleteImage(userInfo);

        File file = fileStorageService.saveFile(image);
        userInfo.setImage(file);
        log.error(userInfo.getImage().toString());
        userInfoRepository.save(userInfo);
    }

    @Override
    public void deleteImage(UserInfo userInfo) {
        if (userInfo.getImage() != null) {
            File image = userInfo.getImage();
            userInfo.setImage(null);
            fileStorageService.delete(image);
        }
    }
}
