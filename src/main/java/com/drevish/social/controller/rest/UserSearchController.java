package com.drevish.social.controller.rest;

import com.drevish.social.controller.dto.UserSearchDto;
import com.drevish.social.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search/user")
public class UserSearchController {
    @Autowired
    private UserSearchService userSearchService;

    @GetMapping("/{nameOrSurname}")
    public List<UserSearchDto> findUsersByNameOrSurname(@PathVariable String nameOrSurname) {
        return userSearchService.findAllByNameOrSurname(nameOrSurname)
                .stream()
                .map(UserSearchDto::assemble)
                .collect(Collectors.toList());
    }
}
