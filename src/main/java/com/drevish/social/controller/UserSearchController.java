package com.drevish.social.controller;

import com.drevish.social.controller.dto.UserSearchDto;
import com.drevish.social.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/search/user")
public class UserSearchController extends ControllerWithUserInfo {
    @Value("${view.search_user}")
    private String searchUserView;

    @Autowired
    private UserSearchService userSearchService;

    @GetMapping
    public String findUsersByNameOrSurname(@RequestParam(name = "q") String nameOrSurname,
                                           Model model) {
        model.addAttribute("users", userSearchService.findAllByNameOrSurname(nameOrSurname)
                .stream()
                .map(UserSearchDto::assemble)
                .collect(Collectors.toList()));
        return searchUserView;
    }
}
