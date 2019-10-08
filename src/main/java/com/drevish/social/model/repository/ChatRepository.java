package com.drevish.social.model.repository;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByUsersIsIn(User user);
}
