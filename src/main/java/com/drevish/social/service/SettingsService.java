package com.drevish.social.service;

import com.drevish.social.exception.InvalidPasswordException;
import com.drevish.social.model.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public interface SettingsService {
    void changeEmail(User user, @Email(message = "Invalid email") String email);

    void changePassword(User user, String oldPassword,
                        @NotNull
                        @Pattern(regexp = "[a-z,,A-Z,0-9,_]*",
                                message = "Only latin letters, numbers and _ are allowed")
                        @Length(min = 3, max = 20, message = "Password should have between 3 and 20 symbols")
                                String newPassword) throws InvalidPasswordException;
}
