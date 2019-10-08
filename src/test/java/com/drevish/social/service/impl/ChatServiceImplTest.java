package com.drevish.social.service.impl;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.Message;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.ChatRepository;
import com.drevish.social.model.repository.MessageRepository;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ChatServiceImplTest {
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
        user1 = new User(1L, "user1", "encrypted", null);
        user2 = new User(2L, "user2", "encrypted", null);
        chat = new Chat(new HashSet<>(Arrays.asList(user1, user2)));
    }

    @Test
    public void shouldReturnExistingDialog() {
        when(chatRepository.findAllByUsersIsIn(user1)).thenReturn(Collections.singletonList(chat));
        when(chatRepository.findAllByUsersIsIn(user1)).thenReturn(Collections.singletonList(chat));

        Chat chat1 = chatService.openNewOrGetExistingDialogue(user1, user2);
        Chat chat2 = chatService.openNewOrGetExistingDialogue(user1, user2);

        assertEquals(chat, chat1);
        assertEquals(chat, chat2);
        verify(chatRepository, times(2)).findAllByUsersIsIn(any(User.class));
        verifyNoMoreInteractions(chatRepository);
    }

    @Test
    public void shouldSendMessage() {
        chatService.sendMessage(chat, user1, "text");
        Message message = new Message(user1, "text", chat, LocalDate.now());
        verify(messageRepository, times(1)).save(message);
    }

    @TestConfiguration
    static class ChatServiceImplTestContextConfiguration {
        @Bean
        public ChatService chatService() {
            return new ChatServiceImpl();
        }
    }
}