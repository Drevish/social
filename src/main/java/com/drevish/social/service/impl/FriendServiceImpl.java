package com.drevish.social.service.impl;

import com.drevish.social.model.entity.FriendState;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.repository.UserRepository;
import com.drevish.social.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class FriendServiceImpl implements FriendService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public FriendState getRelation(User user, User relative) {
        if (user.getFriends() != null && user.getFriends().contains(relative)) {
            return FriendState.FRIEND;
        }
        if (user.getUpcomingFriendRequests() != null && user.getUpcomingFriendRequests().contains(relative)) {
            return FriendState.UPCOMING_FRIEND_REQUEST;
        }
        if (user.getIncomingFriendRequests() != null && user.getIncomingFriendRequests().contains(relative)) {
            return FriendState.INCOMING_FRIEND_REQUEST;
        }
        return FriendState.NONE;
    }

    @Override
    public void subscribe(User user, User subscribingTo) {
        guaranteeNotNullLists(user, subscribingTo);

        user.getUpcomingFriendRequests().add(subscribingTo);
        subscribingTo.getIncomingFriendRequests().add(user);

        userRepository.save(user);
        userRepository.save(subscribingTo);
    }

    @Override
    public void acceptFriendRequest(User user, User requester) {
        guaranteeNotNullLists(user, requester);

        user.getFriends().add(requester);
        requester.getFriends().add(user);
        user.getIncomingFriendRequests().remove(requester);
        requester.getUpcomingFriendRequests().remove(user);

        userRepository.save(user);
        userRepository.save(requester);
    }

    @Override
    public void deleteFriend(User user, User friend) {
        guaranteeNotNullLists(user, friend);

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        friend.getUpcomingFriendRequests().add(user);
        user.getIncomingFriendRequests().add(friend);

        userRepository.save(user);
        userRepository.save(friend);
    }

    private void guaranteeNotNullLists(User... users) {
        for (User user : users) {
            if (user.getFriends() == null) {
                user.setFriends(new ArrayList<>());
            }
            if (user.getIncomingFriendRequests() == null) {
                user.setIncomingFriendRequests(new ArrayList<>());
            }
            if (user.getUpcomingFriendRequests() == null) {
                user.setUpcomingFriendRequests(new ArrayList<>());
            }
        }
    }
}
