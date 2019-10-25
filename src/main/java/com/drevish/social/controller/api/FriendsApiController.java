package com.drevish.social.controller.api;

import com.drevish.social.controller.api.dto.FriendsRelationDto;
import com.drevish.social.model.entity.User;
import com.drevish.social.service.FriendService;
import com.drevish.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/friends")
//TODO: test
public class FriendsApiController {
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
    public FriendsRelationDto getRelation(@PathVariable Long userId, @PathVariable Long relativeId) {
        User user = getUser(userId);
        User relative = getUser(relativeId);
        return new FriendsRelationDto(friendService.getRelation(user, relative).toString());
    }

    @PostMapping("/subscribe")
    @ResponseStatus(OK)
    public void subscribe(@RequestParam Long userId, Principal principal) {
        User user = getUser(principal.getName());
        User subscribingTo = getUser(userId);
        friendService.subscribe(user, subscribingTo);
    }

    @PostMapping("/accept")
    @ResponseStatus(OK)
    public void acceptFriendRequest(@RequestParam Long userId, Principal principal) {
        User user = getUser(principal.getName());
        User requester = getUser(userId);
        friendService.acceptFriendRequest(user, requester);
    }

    @PostMapping("/delete")
    @ResponseStatus(OK)
    public void deleteFriend(@RequestParam Long userId, Principal principal) {
        User user = getUser(principal.getName());
        User friend = getUser(userId);
        friendService.deleteFriend(user, friend);
    }

    @PostMapping("/unsubscribe")
    @ResponseStatus(OK)
    public void unsubscribe(@RequestParam Long userId, Principal principal) {
        User user = getUser(principal.getName());
        User subscribedTo = getUser(userId);
        friendService.unsubscribe(user, subscribedTo);
    }

    private User getUser(Long userId) {
        return userService.getUserById(userId);
    }

    private User getUser(String email) {
        return userService.getUserByEmail(email);
    }

    private List<Long> mapUsersToIds(List<User> users) {
        return users.stream().map(User::getId).collect(Collectors.toList());
    }
}
