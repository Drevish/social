package com.drevish.social.controller.dto;

import com.drevish.social.anno.Name;
import com.drevish.social.anno.Surname;
import lombok.Data;

@Data
public class UserInfo {
    @Name
    private String name;
    @Surname
    private String surname;
}
