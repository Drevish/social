package com.drevish.social.controller;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.ChatService;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/chat")
public class ChatController extends ControllerWithUserInfo {
    @Value("${view.chats_all}")
    private String chatsView;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAllChats(Principal principal, Model model) {
        User user = userService.getUserByEmail(principal.getName());
        List<Chat> chats = chatService.getAllForUser(user);
        model.addAttribute("chats", chats);
        model.addAttribute("user", user);

        Map<Long, UserInfo> userInfosForDialogues = new HashMap<>();
        chats.stream()
                .filter(c -> c.getUsers().size() == 2)
                .forEach(c -> c.getUsers().stream()
                        .filter(u -> !u.equals(user))
                        .forEach(u -> userInfosForDialogues.put(u.getId(),
                                userInfoService.getUserInfoByEmail(u.getEmail()))));
        model.addAttribute("userInfos", userInfosForDialogues);
        return chatsView;
    }
}
