package com.drevish.social.config;

import com.drevish.social.model.entity.Role;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSecurity {
    @Autowired
    private UserService userService;

    public boolean hasChatPermission(Authentication authentication, Long chatId) {
        List<Role> roles = userService.getUserByEmail(authentication.getName()).getRoles();
        return roles.stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase("CHAT_" + chatId));
    }
}
