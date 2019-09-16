package com.drevish.social.service;

import com.drevish.social.anno.Email;
import com.drevish.social.anno.Password;
import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;

public interface SettingsService {
    void changeEmail(User user, @Email String email);

    void changePassword(User user, String oldPassword, @Password String newPassword) throws InvalidPasswordException;
}
