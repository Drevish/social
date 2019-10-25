package com.drevish.social.controller.api.dto;

import com.drevish.social.model.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoDto {
    private Long id;
    private String name;
    private String surname;
    private String imageId;

    static public UserInfoDto assemble(UserInfo userInfo) {
        String imageId = (userInfo.getImage() == null) ? null : userInfo.getImage().getId();
        return new UserInfoDto(userInfo.getId(), userInfo.getName(), userInfo.getSurname(), imageId);
    }
}
