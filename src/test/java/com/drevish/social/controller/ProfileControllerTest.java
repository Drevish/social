package com.drevish.social.controller;

import com.drevish.social.config.CustomAccessDeniedHandler;
import com.drevish.social.exception.UserNotFoundException;
import com.drevish.social.model.entity.FriendState;
import com.drevish.social.model.entity.User;
import com.drevish.social.model.entity.UserInfo;
import com.drevish.social.service.FriendService;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.drevish.social.TestUtils.testUser;
import static com.drevish.social.TestUtils.testUserInfo;
import static com.drevish.social.model.entity.FriendState.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProfileController.class, CustomAccessDeniedHandler.class})
@AutoConfigureMockMvc
@WithMockUser(username = "email@email.com")
public class ProfileControllerTest extends ControllerTestWithUserAndUserInfo {
    @MockBean
    private FriendService friendService;

    @Autowired
    private MockMvc mockMvc;

    private User anotherUser;

    @Before
    public void before() {
        anotherUser = User.builder().id(2L).build();
        when(userService.getUserById(2L)).thenReturn(anotherUser);
    }

    @Test
    public void shouldShowOwnProfilePage() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/profile"))
                .andExpect(status().isOk());
        verifyProfilePageModelAttributes(resultActions);
    }

    private ResultActions verifyProfilePageModelAttributes(ResultActions actions) throws Exception {
        return actions.andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(content().string(StringContains.containsString(testUserInfo.getName())))
                .andExpect(content().string(StringContains.containsString(testUserInfo.getSurname())))
                .andExpect(content().string(StringContains.containsString(testUser.getEmail())));
    }

    @Test
    public void shouldShowAnotherUserPage() throws Exception {
        User anotherUser = User.builder()
                .id(2L)
                .email("another@gmail.com")
                .build();
        UserInfo anotherUserInfo = new UserInfo("anotherName", "anotherSurname");
        when(userService.getUserById(2L)).thenReturn(anotherUser);
        when(userInfoService.getUserInfoByEmail("another@gmail.com")).thenReturn(anotherUserInfo);
        when(friendService.getRelation(testUser, anotherUser)).thenReturn(NONE);

        mockMvc.perform(get("/profile/id{id}", anotherUser.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userInfo", testUserInfo))
                .andExpect(model().attribute("otherUserInfo", anotherUserInfo))
                .andExpect(content().string(StringContains.containsString(anotherUserInfo.getName())))
                .andExpect(content().string(StringContains.containsString(anotherUserInfo.getSurname())));
    }

    @Test
    public void shouldRedirectToProfilePageIfIdIsTheCurrentUserId() throws Exception {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        mockMvc.perform(get("/profile/id{id}", testUser.getId()))
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void shouldRedirectToProfilePageIfInvalidUserId() throws Exception {
        when(userService.getUserById(0L)).thenThrow(new UserNotFoundException(""));
        mockMvc.perform(get("/profile/id{id}", 0L))
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    public void shouldUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("image", "image.png", "image/PNG", "data".getBytes());
        ResultActions actions = mockMvc.perform(multipart("/profile")
                .file(file)
                .with(csrf())
                .param("change", "upload-image"))
                .andExpect(status().isOk());
        verifyProfilePageModelAttributes(actions);
        verify(userInfoService, times(1)).setImage(file, testUserInfo);
    }


    @Test
    public void shouldDeleteImage() throws Exception {
        ResultActions actions = mockMvc.perform(post("/profile")
                .with(csrf())
                .param("change", "delete-image"))
                .andExpect(status().isOk());
        verifyProfilePageModelAttributes(actions);
        verify(userInfoService, times(1)).deleteImage(testUserInfo);
    }

    @Test
    public void shouldDoNothingOnSubscribe() throws Exception {
        callFriendMethodForWrongStates(NONE, "subscribe");
        verify(friendService, never()).subscribe(testUser, anotherUser);
    }

    @Test
    public void shouldSubscribe() throws Exception {
        callFriendMethod(NONE, "subscribe");
        verify(friendService, times(1)).subscribe(testUser, anotherUser);
    }

    @Test
    public void shouldDoNothingOnUnsubscribe() throws Exception {
        callFriendMethodForWrongStates(UPCOMING_FRIEND_REQUEST, "unsubscribe");
        verify(friendService, never()).unsubscribe(testUser, anotherUser);
    }

    @Test
    public void shouldUnsubscribe() throws Exception {
        callFriendMethod(UPCOMING_FRIEND_REQUEST, "unsubscribe");
        verify(friendService, times(1)).unsubscribe(testUser, anotherUser);
    }

    @Test
    public void shouldDoNothingOnAcceptFriendRequest() throws Exception {
        callFriendMethodForWrongStates(INCOMING_FRIEND_REQUEST, "acceptFriendRequest");
        verify(friendService, never()).acceptFriendRequest(testUser, anotherUser);
    }

    @Test
    public void shouldAcceptFriendRequest() throws Exception {
        callFriendMethod(INCOMING_FRIEND_REQUEST, "acceptFriendRequest");
        verify(friendService, times(1)).acceptFriendRequest(testUser, anotherUser);
    }

    @Test
    public void shouldDoNothingOnDeleteFriend() throws Exception {
        callFriendMethodForWrongStates(FRIEND, "deleteFriend");
        verify(friendService, never()).deleteFriend(testUser, anotherUser);
    }

    @Test
    public void shouldDeleteFriend() throws Exception {
        callFriendMethod(FRIEND, "deleteFriend");
        verify(friendService, times(1)).deleteFriend(testUser, anotherUser);
    }

    private void callFriendMethodForWrongStates(FriendState requiredState, String methodName) throws Exception {
        for (FriendState state : FriendState.values()) {
            if (!state.equals(requiredState)) {
                callFriendMethod(state, methodName);
            }
        }
    }

    private void callFriendMethod(FriendState requiredState, String methodName) throws Exception {
        when(friendService.getRelation(testUser, anotherUser)).thenReturn(requiredState);
        mockMvc.perform(post("/profile/id{userId}", 2)
                .param("change", methodName)
                .with(csrf()))
                .andExpect(redirectedUrl("/profile/id2"));
    }

}