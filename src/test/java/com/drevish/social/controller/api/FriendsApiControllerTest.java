package com.drevish.social.controller.api;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.controller.ControllerTestWithUserAndUserInfo;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.FriendService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.drevish.social.TestUtils.testUser;
import static com.drevish.social.model.entity.FriendState.FRIEND;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {FriendsApiController.class, CustomAccessDeniedHandler.class})
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class FriendsApiControllerTest extends ControllerTestWithUserAndUserInfo {

    @MockBean
    private FriendService friendService;

    @Autowired
    private MockMvc mockMvc;

    private User friend;
    private User subscriber;
    private User subscribingTo;

    @Before
    public void before() {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);

        friend = User.builder().id(2L).build();
        when(userService.getUserById(2L)).thenReturn(friend);
        testUser.setFriends(Collections.singletonList(friend));

        subscriber = User.builder().id(3L).build();
        when(userService.getUserById(3L)).thenReturn(subscriber);
        testUser.setIncomingFriendRequests(Collections.singletonList(subscriber));

        subscribingTo = User.builder().id(4L).build();
        when(userService.getUserById(4L)).thenReturn(subscribingTo);
        testUser.setUpcomingFriendRequests(Collections.singletonList(subscribingTo));
    }

    @Test
    public void shouldGetAllFriendIds() throws Exception {
        shouldGetSingletonIdList("/api/friends/friend/1", 2);
    }

    @Test
    public void shouldGetAllIncomingFriendRequesterIds() throws Exception {
        shouldGetSingletonIdList("/api/friends/incoming/1", 3);
    }


    @Test
    public void shouldGetAllUpcomingFriendRequesterIds() throws Exception {
        shouldGetSingletonIdList("/api/friends/upcoming/1", 4);
    }

    private void shouldGetSingletonIdList(String url, int expectedId) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", is(expectedId)));
    }

    @Test
    public void shouldGetUsersRelation() throws Exception {
        when(friendService.getRelation(testUser, friend)).thenReturn(FRIEND);

        mockMvc.perform(get("/api/friends/relation/{userId}/{relativeId}", 1, 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relation", is(FRIEND.toString())));
    }

    @Test
    public void shouldSubscribe() throws Exception {
        User anotherUser = User.builder().id(5L).build();
        when(userService.getUserById(5L)).thenReturn(anotherUser);
        performPostMethod("subscribe", anotherUser.getId());

        verify(friendService, times(1)).subscribe(testUser, anotherUser);
        verifyNoMoreInteractions(friendService);
    }

    @Test
    public void shouldUnsubscribe() throws Exception {
        performPostMethod("unsubscribe", subscribingTo.getId());
        verify(friendService, times(1)).unsubscribe(testUser, subscribingTo);
        verifyNoMoreInteractions(friendService);
    }


    @Test
    public void shouldAcceptFriendRequest() throws Exception {
        performPostMethod("accept", subscriber.getId());
        verify(friendService, times(1)).acceptFriendRequest(testUser, subscriber);
        verifyNoMoreInteractions(friendService);
    }

    @Test
    public void shouldDeleteFriend() throws Exception {
        performPostMethod("delete", friend.getId());
        verify(friendService, times(1)).deleteFriend(testUser, friend);
        verifyNoMoreInteractions(friendService);
    }

    private void performPostMethod(String methodName, Long userId) throws Exception {
        mockMvc.perform(post("/api/friends/" + methodName)
                .param("userId", userId.toString())
                .with(csrf()))
                .andExpect(status().isOk());
    }
}