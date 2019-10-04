package com.drevish.social.service.impl;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.Message;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.ChatRepository;
import com.drevish.social.model.repository.MessageRepository;
import com.drevish.social.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Chat openNewOrGetExisting(User user1, User user2) {
        List<User> users = Arrays.asList(user1, user2);
        System.err.println(users);
        Optional<Chat> optionalChat = chatRepository.findByUsers(Arrays.asList(users));
        if (optionalChat.isPresent()) {
            return optionalChat.get();
        } else {
            Chat chat = new Chat(Arrays.asList(user1, user2));
            chatRepository.save(chat);
            return chat;
        }
    }

    @Override
    public void sendMessage(Chat chat, User sender, String text) {
        Message message = new Message(sender, text, chat, LocalDate.now());
        messageRepository.save(message);
    }
}
