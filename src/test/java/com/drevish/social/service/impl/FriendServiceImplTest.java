package com.drevish.social.service.impl;

import com.drevish.social.model.entity.FriendState;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.FriendService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(SpringRunner.class)
public class FriendServiceImplTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private FriendService friendService;

    private User user;
    private User friend;
    private User userSubscribedTo;
    private User subscriber;

    @Before
    public void before() {
        user = new User();

        friend = new User();
        user.setFriends(mutableSingletonList(friend));
        friend.setFriends(mutableSingletonList(user));

        userSubscribedTo = new User();
        user.setUpcomingFriendRequests(mutableSingletonList(userSubscribedTo));
        userSubscribedTo.setIncomingFriendRequests(mutableSingletonList(user));

        subscriber = new User();
        user.setIncomingFriendRequests(mutableSingletonList(subscriber));
        subscriber.setUpcomingFriendRequests(mutableSingletonList(user));

        user.setId(1L);
        friend.setId(2L);
        userSubscribedTo.setId(3L);
        subscriber.setId(4L);
    }

    private ArrayList<User> mutableSingletonList(User user) {
        ArrayList<User> list = new ArrayList<>();
        list.add(user);
        return list;
    }

    @Test
    public void shouldReturnFriendsRelation() {
        FriendState relation = friendService.getRelation(user, friend);
        assertEquals(FriendState.FRIEND, relation);
    }

    @Test
    public void shouldReturnUpcomingFriendRequestRelation() {
        FriendState relation = friendService.getRelation(user, userSubscribedTo);
        assertEquals(FriendState.UPCOMING_FRIEND_REQUEST, relation);
    }

    @Test
    public void shouldReturnIncomingFriendRequestRelation() {
        FriendState relation = friendService.getRelation(user, subscriber);
        assertEquals(FriendState.INCOMING_FRIEND_REQUEST, relation);
    }

    @Test
    public void shouldReturnNoneRelation() {
        FriendState relation = friendService.getRelation(user, new User());
        assertEquals(FriendState.NONE, relation);
    }

    @Test
    public void shouldSubscribe() {
        User subscribingTo = new User();
        friendService.subscribe(user, subscribingTo);
        verifyOnlySavedThese(user, subscribingTo);

        assertEquals(FriendState.UPCOMING_FRIEND_REQUEST, friendService.getRelation(user, subscribingTo));
        assertEquals(FriendState.INCOMING_FRIEND_REQUEST, friendService.getRelation(subscribingTo, user));
    }

    @Test
    public void shouldAcceptFriendRequest() {
        friendService.acceptFriendRequest(user, subscriber);
        verifyOnlySavedThese(user, subscriber);

        assertEquals(FriendState.FRIEND, friendService.getRelation(user, subscriber));
        assertEquals(FriendState.FRIEND, friendService.getRelation(subscriber, user));
    }

    @Test
    public void shouldDeleteFriend() {
        friendService.deleteFriend(user, friend);
        verifyOnlySavedThese(user, friend);

        assertEquals(FriendState.UPCOMING_FRIEND_REQUEST, friendService.getRelation(friend, user));
        assertEquals(FriendState.INCOMING_FRIEND_REQUEST, friendService.getRelation(user, friend));
    }

    @Test
    public void shouldUnsubscribe() {
        friendService.unsubscribe(user, userSubscribedTo);
        verifyOnlySavedThese(user, userSubscribedTo);

        assertEquals(FriendState.NONE, friendService.getRelation(userSubscribedTo, user));
        assertEquals(FriendState.NONE, friendService.getRelation(user, userSubscribedTo));
    }

    private void verifyOnlySavedThese(User... users) {
        for (User user : users) {
            verify(userRepository).save(user);
        }
        verifyNoMoreInteractions(userRepository);
    }

    @TestConfiguration
    static class FriendServiceImplTestContextConfiguration {
        @Bean
        public FriendService userService() {
            return new FriendServiceImpl();
        }
    }
}
