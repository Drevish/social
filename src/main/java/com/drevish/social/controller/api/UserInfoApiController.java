package com.drevish.social.controller.api;

import com.drevish.social.controller.api.dto.UserInfoDto;
import com.drevish.social.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userInfo")
//TODO: test
public class UserInfoApiController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/{id}")
    public UserInfoDto getInfoById(@PathVariable Long id) {
        return UserInfoDto.assemble(userInfoService.getUserInfoById(id));
    }
}
