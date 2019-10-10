package com.drevish.social.model.repository;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByUsersIsIn(User user);

    Optional<Chat> findById(Long id);
}
