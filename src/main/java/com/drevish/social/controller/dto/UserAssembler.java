package com.drevish.social.controller.dto;

import com.drevish.social.model.entity.User;

public final class UserAssembler {
    private UserAssembler() {
    }

    public static User assemble(UserRegistrationInfo info) {
        return User.builder()
                .name(info.getName())
                .surname(info.getSurname())
                .email(info.getEmail())
                .password(info.getPassword()).build();
    }
}
