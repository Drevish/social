package com.drevish.social.controller;

import com.drevish.social.exception.InvalidImageException;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.FriendState;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.FriendService;
import com.drevish.social.service.UserService;
import com.drevish.social.util.BiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping("/profile")
public class ProfileController extends ControllerWithUserInfo {
    @Value("${view.profile}")
    private String profileView;

    @Value("${view.other-profile}")
    private String otherProfileView;

    @Value("${path.profile}")
    private String profilePath;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @GetMapping
    public String showOwnProfile(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        return profileView;
    }

    @GetMapping("/id{userId}")
    public String showUserProfile(@PathVariable Long userId, Principal principal, Model model) {
        User user;
        try {
            user = userService.getUserById(userId);
        } catch (UserNotFoundException e) {
            log.warn("User not found with id " + userId);
            return "redirect:" + profilePath;
        }

        if (user.getEmail().equals(principal.getName())) {
            return "redirect:" + profilePath;
        }

        UserInfo userInfo = userInfoService.getUserInfoByEmail(user.getEmail());
        model.addAttribute("otherUserInfo", userInfo);

        User current = userService.getUserByEmail(principal.getName());
        FriendState relation = friendService.getRelation(current, user);
        model.addAttribute("relation", relation);

        return otherProfileView;
    }

    @PostMapping(value = "/id{userId}", params = "change=subscribe")
    public String subscribe(@PathVariable Long userId, Principal principal) {
        return friendMethod(FriendState.NONE, (t, r) -> friendService.subscribe(t, r),
                userId, principal);
    }

    @PostMapping(value = "/id{userId}", params = "change=unsubscribe")
    public String unsubscribe(@PathVariable Long userId, Principal principal) {
        return friendMethod(FriendState.UPCOMING_FRIEND_REQUEST, (t, r) -> friendService.unsubscribe(t, r),
                userId, principal);
    }

    @PostMapping(value = "/id{userId}", params = "change=acceptFriendRequest")
    public String acceptFriendRequest(@PathVariable Long userId, Principal principal) {
        return friendMethod(FriendState.INCOMING_FRIEND_REQUEST, (t, r) -> friendService.acceptFriendRequest(t, r),
                userId, principal);
    }

    @PostMapping(value = "/id{userId}", params = "change=deleteFriend")
    public String deleteFriend(@PathVariable Long userId, Principal principal) {
        return friendMethod(FriendState.FRIEND, (t, r) -> friendService.deleteFriend(t, r),
                userId, principal);
    }

    private String friendMethod(FriendState requiredRelation, BiOperation<User, User> operator,
                                Long userId, Principal principal) {
        User another = userService.getUserById(userId);
        User user = userService.getUserByEmail(principal.getName());

        FriendState relation = friendService.getRelation(user, another);
        if (relation.equals(requiredRelation)) {
            operator.run(user, another);
        }
        return "redirect:/profile/id" + userId;
    }

    @PostMapping(params = "change=upload-image")
    public String updateImage(@RequestParam MultipartFile image, Principal principal, Model model) {
        try {
            userInfoService.setImage(image, userInfo(principal));
        } catch (InvalidImageException e) {
            log.warn(e.toString());
        }
        return "redirect:/profile";
    }

    @PostMapping(params = "change=delete-image")
    public String deleteImage(Principal principal, Model model) {
        UserInfo userInfo = userInfo(principal);
        userInfoService.deleteImage(userInfo);
        return "redirect:/profile";
    }
}
