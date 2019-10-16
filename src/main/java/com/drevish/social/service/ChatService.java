package com.drevish.social.service;

import com.drevish.social.exception.ChatNotFoundException;
import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;

import java.util.List;

public interface ChatService {
    List<Chat> getAllForUser(User user);

    Chat openNewOrGetExistingDialogue(User user1, User user2);

    Chat getById(Long id) throws ChatNotFoundException;

    void sendMessage(Chat chat, User sender, String text);
}
