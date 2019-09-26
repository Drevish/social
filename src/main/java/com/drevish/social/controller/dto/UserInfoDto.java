package com.drevish.social.controller.dto;

import com.drevish.social.anno.Name;
import com.drevish.social.anno.Surname;
import com.drevish.social.model.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    @Name
    private String name;

    @Surname
    private String surname;

    public static UserInfoDto assemble(UserInfo userInfo) {
        return new UserInfoDto(userInfo.getName(), userInfo.getSurname());
    }

    public UserInfo assemble() {
        return new UserInfo(name, surname);
    }
}