package com.drevish.social.model.repository;

import com.drevish.social.model.entity.Chat;
import com.drevish.social.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c from Chat c where c.users IN :users")
    Optional<Chat> findByUsers(List<List<User>> users);
}
