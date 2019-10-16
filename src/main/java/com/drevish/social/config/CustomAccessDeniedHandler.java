package com.drevish.social.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/chat/")) {
            response.sendRedirect("/chat");
        } else {
            response.sendRedirect("/");
        }
    }
}
