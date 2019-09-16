package com.drevish.social.config;

import com.drevish.social.model.entity.User;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth)
            throws IOException, ServletException {
        super.onAuthenticationSuccess(req, resp, auth);
        HttpSession session = req.getSession();
        User user = userService.getUserByEmail(auth.getName());
        session.setAttribute("user", user);
        log.info("User with email" + user.getEmail() + " logged in. Session attribute was created.");
    }
}
