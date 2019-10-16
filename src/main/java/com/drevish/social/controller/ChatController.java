package com.drevish.social.controller;

import com.drevish.social.exception.ChatNotFoundException;
import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.ChatService;
import com.drevish.social.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/chat")
public class ChatController extends ControllerWithUserInfo {
    @Value("${path.chat}")
    private String chatsPath;

    @Value("${view.chats_all}")
    private String chatsView;

    @Value("${view.chat}")
    private String chatView;

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
        model.addAttribute("userInfos", buildUserIdUserInfoMap(user, chats));
        return chatsView;
    }

    private Map<Long, UserInfo> buildUserIdUserInfoMap(User user, List<Chat> chats) {
        Map<Long, UserInfo> userInfosForDialogues = new HashMap<>();
        chats.stream()
                .filter(c -> c.getUsers().size() == 2)
                .forEach(c -> c.getUsers().stream()
                        .filter(u -> !u.equals(user))
                        .forEach(u -> userInfosForDialogues.put(u.getId(),
                                userInfoService.getUserInfoByEmail(u.getEmail()))));
        return userInfosForDialogues;
    }

    @GetMapping("/{id}")
    public String showChat(@PathVariable Long id, Principal principal, Model model) {
        try {
            Chat chat = chatService.getById(id);
            model.addAttribute("chat", chat);
            User user = userService.getUserByEmail(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("userInfos", buildUserIdUserInfoMap(user, Collections.singletonList(chat)));
            return chatView;
        } catch (ChatNotFoundException e) {
            log.warn(e.toString());
            return "redirect:" + chatsPath;
        }
    }

    @PostMapping("/{id}/send")
    public String sendMessage(@PathVariable Long id, @RequestParam String text, Principal principal) {
        try {
            chatService.sendMessage(chatService.getById(id), userService.getUserByEmail(principal.getName()), text);
            return "redirect:/chat/" + id;
        } catch (ChatNotFoundException e) {
            log.warn(e.toString());
            return "redirect:" + chatsPath;
        }
    }
}
