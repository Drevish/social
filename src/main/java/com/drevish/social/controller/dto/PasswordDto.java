package com.drevish.social.controller.dto;

import com.drevish.social.anno.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    @Password
    private String password;
}
