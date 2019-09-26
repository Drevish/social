package com.drevish.social.service;

import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CustomUserDetailsServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService service;

    @Test
    public void shouldLoadUserDetailsByUsername() {
        String username = "username";
        User user = User.builder()
                .email(username)
                .password("password")
                .roles(new ArrayList<>()).build();
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        UserDetails details = service.loadUserByUsername(username);

        verify(userRepository, times(1)).findByEmail(username);
        assertEquals(username, details.getUsername());
        assertEquals(user.getPassword(), details.getPassword());
        assertTrue(details.getAuthorities().isEmpty());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CustomUserDetailsService customUserDetailsService() {
            return new CustomUserDetailsService();
        }
    }
}