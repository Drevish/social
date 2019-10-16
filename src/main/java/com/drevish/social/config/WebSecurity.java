package com.drevish.social.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {
    public boolean hasChatPermission(Authentication authentication, Long chatId) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equalsIgnoreCase("CHAT_" + chatId));
    }
}
