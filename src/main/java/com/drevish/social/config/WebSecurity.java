package com.drevish.social.config;

import com.drevish.social.model.entity.Role;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {
    @Autowired
    private UserService userService;

    public boolean hasChatPermission(Authentication authentication, Long chatId) {
        updateAuthorities(authentication);
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("CHAT_" + chatId));
    }

    private void updateAuthorities(Authentication authentication) {
        User user = userService.getUserByEmail(authentication.getName());
        String[] userRoles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);
        UsernamePasswordAuthenticationToken auth = new
                UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(),
                AuthorityUtils.createAuthorityList(userRoles));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
