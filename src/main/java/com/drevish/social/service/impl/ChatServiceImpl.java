package com.drevish.social.service.impl;

import com.drevish.social.exception.ChatNotFoundException;
import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.Message;
import com.drevish.social.model.entity.Role;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.ChatRepository;
import com.drevish.social.model.repository.MessageRepository;
import com.drevish.social.model.repository.RoleRepository;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Chat> getAllForUser(User user) {
        return chatRepository.findAllByUsersIsIn(user);
    }

    @Override
    public Chat openNewOrGetExistingDialogue(User user1, User user2) {
        List<Chat> allChatsWithUser1 = chatRepository.findAllByUsersIsIn(user1);
        Optional<Chat> dialogue = allChatsWithUser1.stream()
                .filter(c -> c.getUsers().size() == 2)
                .filter(c -> c.getUsers().stream()
                        .anyMatch(u -> u.getId().equals(user2.getId())))
                .findFirst();

        return dialogue.orElseGet(() -> createDialogue(user1, user2));
    }

    private Chat createDialogue(User user1, User user2) {
        Chat chat = new Chat(Stream.of(user1, user2).collect(Collectors.toSet()));
        chatRepository.save(chat);
        Long id = chat.getId();

        Role role = new Role("CHAT_" + id);
        roleRepository.save(role);
        assignRoleToUsers(Arrays.asList(user1, user2), role);
        return chat;
    }

    private void assignRoleToUsers(List<User> users, Role role) {
        users
                .forEach(u -> {
                    u.getRoles().add(role);
                    userRepository.save(u);
                });
    }

    @Override
    public Chat getById(Long id) {
        return chatRepository.findById(id).orElseThrow(
                () -> new ChatNotFoundException("No chat found with id " + id)
        );
    }

    @Override
    public void sendMessage(Chat chat, User sender, String text) {
        Message message = new Message(sender, text, chat, LocalDateTime.now());
        messageRepository.save(message);
    }
}
