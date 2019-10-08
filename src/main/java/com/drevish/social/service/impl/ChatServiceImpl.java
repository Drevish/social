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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Chat> getAllForUser(User user) {
        return chatRepository.findAllByUsersIsIn(user);
    }

    @Override
    public Chat openNewOrGetExistingDialogue(User user1, User user2) {
        List<Chat> allChatsWithUser1 = chatRepository.findAllByUsersIsIn(user1);
        Optional<Chat> dialogue = allChatsWithUser1.stream().
                filter(c -> c.getUsers().size() == 2)
                .filter(c -> c.getUsers().stream()
                        .anyMatch(u -> u.getId().equals(user2.getId())))
                .findFirst();

        if (dialogue.isPresent()) {
            return dialogue.get();
        } else {
            Chat chat = new Chat(Stream.of(user1, user2).collect(Collectors.toSet()));
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
