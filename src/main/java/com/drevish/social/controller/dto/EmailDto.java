package com.drevish.social.controller.dto;

import com.drevish.social.anno.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {
    @Email
    private String email;
}
