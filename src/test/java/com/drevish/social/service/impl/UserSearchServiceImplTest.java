package com.drevish.social.service.impl;

import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.model.repository.UserInfoRepository;
import com.drevish.social.service.UserSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserSearchServiceImplTest {

    @MockBean
    private UserInfoRepository repository;

    @Autowired
    private UserSearchService service;

    @Test
    public void shouldDelegateToUserInfoRepository() {
        String nameOrSurname = "nameOrSurname";
        List<UserInfo> allUsers = Arrays.asList(new UserInfo(),
                new UserInfo("name", "surname"));
        when(repository.findAllByNameStartsWithOrSurnameStartsWith(nameOrSurname, nameOrSurname)).thenReturn(allUsers);

        List<UserInfo> allByNameOrSurname = service.findAllByNameOrSurname(nameOrSurname);
        verify(repository, times(1)).
                findAllByNameStartsWithOrSurnameStartsWith(nameOrSurname, nameOrSurname);
        assertEquals(allUsers, allByNameOrSurname);
    }

    @TestConfiguration
    static class SearchUserServiceImplTestContextConfiguration {
        @Bean
        public UserSearchService searchUserService() {
            return new UserSearchServiceImpl();
        }
    }
}