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
public class UserSearchDto {
    private Long id;

    @Name
    private String name;

    @Surname
    private String surname;

    public static UserSearchDto assemble(UserInfo userInfo) {
        return new UserSearchDto(userInfo.getId(), userInfo.getName(), userInfo.getSurname());
    }
}