package com.drevish.social.service;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;

public interface ChatService {
    Chat openNewOrGetExistingDialogue(User user1, User user2);

    void sendMessage(Chat chat, User sender, String text);
}
