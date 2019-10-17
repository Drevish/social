package com.drevish.social.controller.api;

import com.drevish.social.controller.api.dto.UserSearchApiDto;
import com.drevish.social.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/search/user")
public class UserSearchApiController {
    @Autowired
    private UserSearchService userSearchService;

    @GetMapping("/{nameOrSurname}")
    public List<UserSearchApiDto> findUsersByNameOrSurname(@PathVariable String nameOrSurname) {
        return userSearchService.findAllByNameOrSurname(nameOrSurname)
                .stream()
                .map(UserSearchApiDto::assemble)
                .collect(Collectors.toList());
    }
}
