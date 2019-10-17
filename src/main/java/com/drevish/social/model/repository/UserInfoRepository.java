package com.drevish.social.model.repository;

import com.drevish.social.model.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    List<UserInfo> findAllByNameStartsWithOrSurnameStartsWith(String name, String surname);
}
