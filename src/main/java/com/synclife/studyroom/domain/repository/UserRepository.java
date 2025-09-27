package com.synclife.studyroom.domain.repository;

import com.synclife.studyroom.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLoginIdAndLoginPw(String loginId, String loginPw);
}
