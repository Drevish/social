package com.drevish.social.controller;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.config.WebSecurity;
import com.drevish.social.exception.ChatNotFoundException;
import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.File;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.ChatService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.drevish.social.TestUtils.testUser;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ChatController.class, CustomAccessDeniedHandler.class, WebSecurity.class})
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com", authorities = "CHAT_1")
public class ChatControllerTest extends ControllerTestWithUserAndUserInfo {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    private Chat testChat;
    private List<Chat> testChats;

    @Before
    public void before() {
        User anotherUser = new User(2L, "another@gmail.com", "password", new ArrayList<>());
        testChat = new Chat(Stream.of(testUser, anotherUser).collect(Collectors.toSet()));
        testChat.setId(1L);
        testChat.setMessages(new ArrayList<>());
        testChats = Collections.singletonList(testChat);

        UserInfo anotherUserInfo = new UserInfo(2L, anotherUser, "another", "user",
                new File("img", "image/PNG", new byte[]{}));
        when(userInfoService.getUserInfoByEmail(anotherUser.getEmail())).thenReturn(anotherUserInfo);
        when(chatService.getAllForUser(testUser)).thenReturn(testChats);
    }

    @Test
    public void shouldShowAllChats() throws Exception {
        mockMvc.perform(get("/chat"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("chats", testChats))
                .andExpect(model().attributeExists("userInfos"));
    }

    @Test
    public void shouldShowExactChat() throws Exception {
        when(chatService.getById(1L)).thenReturn(testChat);
        mockMvc.perform(get("/chat/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(model().attribute("chat", testChat))
                .andExpect(model().attributeExists("userInfos"));
    }

    @Test
    public void shouldRedirectToAllChatsIfChatNotFoundAtChatPage() throws Exception {
        when(chatService.getById(1L)).thenThrow(ChatNotFoundException.class);
        mockMvc.perform(get("/chat/{id}", 1))
                .andExpect(redirectedUrl("/chat"));
    }

    @Test
    public void shouldSendMessage() throws Exception {
        when(chatService.getById(1L)).thenReturn(testChat);
        mockMvc.perform(post("/chat/{id}/send", 1)
                .with(csrf())
                .param("text", "message"))
                .andExpect(redirectedUrl("/chat/1"));
        verify(chatService, times(1)).sendMessage(testChat, testUser, "message");
    }

    @Test
    public void shouldRedirectToAllChatsIfChatNotFoundWhenSendingMessage() throws Exception {
        when(chatService.getById(1L)).thenThrow(ChatNotFoundException.class);
        mockMvc.perform(post("/chat/{id}/send", 1)
                .with(csrf())
                .param("text", "message"))
                .andExpect(redirectedUrl("/chat"));
    }

    @Test
    @WithMockUser(username = "email@email.com")
    public void shouldReturnAllChatsIfHasNoChatAuthority() throws Exception {
        mockMvc.perform(get("/chat/{id}", 2))
                .andExpect(redirectedUrl("/chat"));
        mockMvc.perform(post("/chat/{id}/send", 2)
                .with(csrf())
                .param("text", "message"))
                .andExpect(redirectedUrl("/chat"));
    }
}