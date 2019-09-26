package com.drevish.social.service.impl;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SettingsServiceImpl implements SettingsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void changeEmail(User user, String email) {
        user.setEmail(email);
        userRepository.save(user);
        updateUserInSecurityContext(user);
    }

    @Override
    public void changePassword(User user, String oldPassword, String newPassword) {
        checkOldPassword(user, oldPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        updateUserInSecurityContext(user);
    }

    private void checkOldPassword(User user, String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidPasswordException("Incorrect old password");
        }
    }

    @SuppressWarnings("unchecked")
    private void updateUserInSecurityContext(User user) {
        Collection<SimpleGrantedAuthority> nowAuthorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                        .getContext().getAuthentication().getAuthorities();
        UsernamePasswordAuthenticationToken auth = new
                UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
