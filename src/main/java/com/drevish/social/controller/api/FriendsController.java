package com.drevish.social.controller.api;

import com.drevish.social.model.entity.FriendState;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.FriendService;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search/user/friends")
//TODO: test
public class FriendsController {
    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @GetMapping("/friend/{userId}")
    public List<Long> getAllFriendsIds(@PathVariable Long userId) {
        User user = getUser(userId);
        return mapUsersToIds(user.getFriends());
    }

    @GetMapping("/upcoming/{userId}")
    public List<Long> getAllUpcomingFriendRequests(@PathVariable Long userId) {
        User user = getUser(userId);
        return mapUsersToIds(user.getUpcomingFriendRequests());
    }

    @GetMapping("/incoming/{userId}")
    public List<Long> getAllIncomingFriendsIds(@PathVariable Long userId) {
        User user = getUser(userId);
        return mapUsersToIds(user.getIncomingFriendRequests());
    }

    @GetMapping("/relation/{userId}/{relativeId}")
    public FriendState getRelation(@PathVariable Long userId, @PathVariable Long relativeId) {
        User user = getUser(userId);
        User relative = getUser(relativeId);
        return friendService.getRelation(user, relative);
    }

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody Long userId, @RequestBody Long subscribingToId) {
        User user = getUser(userId);
        User subscribingTo = getUser(subscribingToId);
        friendService.subscribe(user, subscribingTo);
    }

    @PostMapping("/accept")
    public void acceptFriendRequest(@RequestBody Long userId, @RequestBody Long requesterId) {
        User user = getUser(userId);
        User requester = getUser(requesterId);
        friendService.acceptFriendRequest(user, requester);
    }

    @PostMapping("/delete")
    public void deleteFriend(@RequestBody Long userId, @RequestBody Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        friendService.deleteFriend(user, friend);
    }

    private User getUser(Long userId) {
        return userService.getUserById(userId);
    }

    private List<Long> mapUsersToIds(List<User> users) {
        return users.stream().map(User::getId).collect(Collectors.toList());
    }
}
