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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ChatServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private ChatRepository chatRepository;

    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    private User user1;
    private User user2;
    private Chat chat;

    @Before
    public void before() {
        user1 = new User(1L, "user1", "encrypted", new ArrayList<>());
        user2 = new User(2L, "user2", "encrypted", new ArrayList<>());
        chat = new Chat(new HashSet<>(Arrays.asList(user1, user2)));
        chat.setId(1L);
    }

    @Test
    public void shouldReturnExistingDialog() {
        when(chatRepository.findAllByUsersIsIn(user1)).thenReturn(Collections.singletonList(chat));
        when(chatRepository.findAllByUsersIsIn(user2)).thenReturn(Collections.singletonList(chat));

        Chat chat1 = chatService.openNewOrGetExistingDialogue(user1, user2);
        Chat chat2 = chatService.openNewOrGetExistingDialogue(user2, user1);

        assertEquals(chat, chat1);
        assertEquals(chat, chat2);
        verify(chatRepository, times(2)).findAllByUsersIsIn(any(User.class));
        verifyNoMoreInteractions(chatRepository);
    }

    @Test
    public void shouldCreateNewDialogAndRoleAndAssignRoleToUsers() {
        when(chatRepository.findAllByUsersIsIn(user1)).thenReturn(Collections.emptyList());
        when(chatRepository.save(any(Chat.class))).then(
                invocation -> {
                    Chat myChat = (Chat) invocation.getArgument(0);
                    myChat.setId(1L);
                    return myChat;
                }
        );
        when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        Chat chat = chatService.openNewOrGetExistingDialogue(user1, user2);
        assertEquals(Stream.of(user1, user2).collect(Collectors.toSet()), chat.getUsers());

        verify(chatRepository, times(1)).findAllByUsersIsIn(any(User.class));
        Role role = new Role("CHAT_1");
        verify(roleRepository, times(1)).save(role);
        user1.getRoles().add(role);
        user2.getRoles().add(role);
        verify(userRepository, times(1)).save(user1);
        verify(userRepository, times(1)).save(user2);
    }

    @Test
    public void shouldSendMessage() {
        chatService.sendMessage(chat, user1, "text");
        Message message = new Message(user1, "text", chat, LocalDate.now());
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    public void shouldDelegateFindAllToRepository() {
        when(chatRepository.findAllByUsersIsIn(user1)).thenReturn(Collections.singletonList(chat));
        List<Chat> resultList = chatService.getAllForUser(user1);
        assertEquals(Collections.singletonList(chat), resultList);
        verify(chatRepository, times(1)).findAllByUsersIsIn(user1);
        verifyNoMoreInteractions(chatRepository);
    }

    @Test
    public void shouldReturnById() {
        when(chatRepository.findById(chat.getId())).thenReturn(Optional.of(chat));
        Chat result = chatService.getById(chat.getId());
        assertEquals(chat, result);
        verify(chatRepository, times(1)).findById(chat.getId());
        verifyNoMoreInteractions(chatRepository);
    }

    @Test(expected = ChatNotFoundException.class)
    public void shouldThrowChatNotFoundException() {
        when(chatRepository.findById(chat.getId())).thenReturn(Optional.empty());
        chatService.getById(chat.getId());
    }

    @TestConfiguration
    static class ChatServiceImplTestContextConfiguration {
        @Bean
        public ChatService chatService() {
            return new ChatServiceImpl();
        }
    }
}