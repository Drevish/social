package com.drevish.social.service.impl;

import com.drevish.social.controller.dto.UserInfoDto;
import com.drevish.social.exception.InvalidImageException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.File;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserInfoRepository;
import com.drevish.social.service.FileStorageService;
import com.drevish.social.service.UserInfoService;
import com.drevish.social.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.drevish.social.TestUtils.testUser;
import static com.drevish.social.TestUtils.testUserInfo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserInfoServiceImplTest {
    @MockBean
    private UserInfoRepository userInfoRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private UserInfoService userInfoService;

    @Mock
    private MultipartFile testImage;

    @Before
    public void before() throws IOException {
        when(testImage.getContentType()).thenReturn("image/PNG");
        when(testImage.getOriginalFilename()).thenReturn("image.png");
        byte[] imageData = "image data".getBytes();
        when(testImage.getSize()).thenReturn(Long.valueOf(imageData.length));
        when(testImage.getBytes()).thenReturn(imageData);
    }

    @Test
    public void shouldReturnUserInfoByEmail() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.of(testUserInfo));
        UserInfo userInfo = userInfoService.getUserInfoByEmail(testUser.getEmail());
        assertEquals(testUserInfo, userInfo);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFoundInUserServiceGetByEmail() {
        when(userService.getUserByEmail(testUser.getEmail())).thenThrow(UserNotFoundException.class);
        userInfoService.getUserInfoByEmail(testUser.getEmail());
    }

    @Test
    public void shouldReturnUserInfoById() {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.of(testUserInfo));
        UserInfo userInfo = userInfoService.getUserInfoById(testUser.getId());
        assertEquals(testUserInfo, userInfo);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFoundInUserServiceGetById() {
        when(userService.getUserById(testUser.getId())).thenThrow(UserNotFoundException.class);
        userInfoService.getUserInfoById(testUser.getId());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionIfUserNotFoundInUserInfoRepository() {
        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.empty());
        userInfoService.getUserInfoByEmail(testUser.getEmail());
    }

    @Test
    public void shouldSaveUserInfo() {
        userInfoService.save(testUserInfo);
        verify(userInfoRepository, times(1)).save(testUserInfo);
    }

    @Test
    public void shouldChangeNameAndSurnameAndSave() {
        UserInfoDto newValues = new UserInfoDto("new name", "new surname");
        when(userInfoRepository.findById(testUser.getId())).thenReturn(Optional.of(testUserInfo));
        userInfoService.updateNameAndSurname(newValues, testUser);

        verify(userInfoRepository, times(1)).findById(testUser.getId());
        verify(userInfoRepository, times(1)).save(testUserInfo);
        assertEquals(newValues.getName(), testUserInfo.getName());
        assertEquals(newValues.getSurname(), testUserInfo.getSurname());
    }

    @Test
    public void shouldDeleteOldImageAndSaveAndSetNewImage() {
        testUserInfo.setImage(new File());
        File savedFile = new File();
        when(fileStorageService.saveFile(testImage)).thenReturn(savedFile);
        userInfoService.setImage(testImage, testUserInfo);

        verify(fileStorageService, times(1)).delete(any());
        verify(fileStorageService, times(1)).saveFile(testImage);
        assertEquals(savedFile, testUserInfo.getImage());
    }

    @Test(expected = InvalidImageException.class)
    public void shouldReturnInvalidImageExceptionIfFileSizeIsZero() {
        when(testImage.getSize()).thenReturn(0L);
        userInfoService.setImage(testImage, testUserInfo);
    }

    @Test(expected = InvalidImageException.class)
    public void shouldReturnInvalidImageExceptionIfFileIsNotAnImage() {
        when(testImage.getContentType()).thenReturn("PDF");
        userInfoService.setImage(testImage, testUserInfo);
    }

    @Test
    public void shouldDeleteImageIfPresent() {
        testUserInfo.setImage(new File());
        userInfoService.deleteImage(testUserInfo);
        verify(fileStorageService, times(1)).delete(any());
        assertNull(testUserInfo.getImage());
    }

    @Test
    public void shouldNotDeleteImageIfNotPresent() {
        testUserInfo.setImage(null);
        userInfoService.deleteImage(testUserInfo);
        verify(fileStorageService, never()).delete(any());
        assertNull(testUserInfo.getImage());
    }

    @TestConfiguration
    static class UserInfoServiceImplTestContextConfiguration {
        @Bean
        public UserInfoService userInfoService() {
            return new UserInfoServiceImpl();
        }
    }
}