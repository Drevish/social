package com.drevish.social.service;

import com.drevish.social.model.entity.User;

public interface SettingsService {
    void changeEmail(User user, String email);

    void changePassword(User user, String oldPassword, String newPassword);
}
