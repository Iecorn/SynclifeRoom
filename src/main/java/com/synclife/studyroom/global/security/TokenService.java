package com.synclife.studyroom.global.security;

import com.synclife.studyroom.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  public String generateToken(User user) {
    if ("ADMIN".equals(user.getRole())) {
      return "admin-token";
    } else {
      return "user-token-" + user.getUserId();
    }
  }

  public UserPayload parseToken(String token) {
    if ("admin-token".equals(token)) {
      return new UserPayload(1L, "ADMIN");
    }
    if (token.startsWith("user-token-")) {
      Long userId = Long.parseLong(token.substring("user-token-".length()));
      return new UserPayload(userId, "USER");
    }
    throw new IllegalArgumentException("유효하지 않은 토큰");
  }
}
