package com.drevish.social.controller.dto;

import com.drevish.social.anno.Name;
import com.drevish.social.anno.Surname;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Name
    private String name;
    @Surname
    private String surname;
}
