package com.drevish.social.service;

import com.drevish.social.model.entity.FriendState;
import com.drevish.social.model.entity.User;

public interface FriendService {
    FriendState getRelation(User user, User relative);

    void subscribe(User user, User subscribingTo);

    void acceptFriendRequest(User user, User requester);

    void deleteFriend(User user, User friend);
}
