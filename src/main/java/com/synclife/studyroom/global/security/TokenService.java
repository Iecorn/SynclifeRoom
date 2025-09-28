package com.synclife.studyroom.global.security;

import com.synclife.studyroom.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

  public String generateToken(User user) {
    String token;
    if ("ADMIN".equals(user.getRole())) {
      token = "admin-token";
    } else {
      token = "user-token-" + user.getUserId();
    }

    log.info("토큰 생성 완료 - userId: {}, role: {}", user.getUserId(), user.getRole());
    return token;
  }

  public UserPayload parseToken(String token) {
    log.debug("토큰 파싱 시도 - token: {}...", token.length() > 10 ? token.substring(0, 10) : token);

    if ("admin-token".equals(token)) {
      log.info("토큰 파싱 성공 - ADMIN 권한");
      return new UserPayload(1L, "ADMIN");
    }
    if (token.startsWith("user-token-")) {
      Long userId = Long.parseLong(token.substring("user-token-".length()));
      log.info("토큰 파싱 성공 - userId: {}, role: USER", userId);
      return new UserPayload(userId, "USER");
    }

    log.warn("토큰 파싱 실패 - 유효하지 않은 토큰 입력");
    throw new IllegalArgumentException("유효하지 않은 토큰");
  }
}
