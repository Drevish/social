package com.drevish.social.service;

import com.drevish.social.anno.Password;
import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public interface SettingsService {
    void changeEmail(User user, @NotNull @Email(message = "Invalid email") String email);

    void changePassword(User user, String oldPassword, @Password String newPassword) throws InvalidPasswordException;
}
