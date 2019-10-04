package com.drevish.social.service.impl;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.ChatService;
import com.drevish.social.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ChatServiceImplTest {
    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @Test
    public void test() {
        User user1 = userService.getUserById(1L);
        User user2 = userService.getUserById(2L);
        System.err.println(user1);
        System.err.println(user2);

        Chat chat1 = chatService.openNewOrGetExisting(user1, user2);
        System.out.println(chat1);
        Chat chat2 = chatService.openNewOrGetExisting(user2, user1);
        System.out.println(chat2);
    }
}