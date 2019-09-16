package com.drevish.social.controller.dto;

import com.drevish.social.anno.Email;
import com.drevish.social.anno.Name;
import com.drevish.social.anno.Password;
import com.drevish.social.anno.Surname;
import lombok.Data;

@Data
public class UserRegistrationInfo {
    @Name
    private String name;

    @Surname
    private String surname;

    @Email
    private String email;

    @Password
    private String password;

    private String passwordCheck;
}
