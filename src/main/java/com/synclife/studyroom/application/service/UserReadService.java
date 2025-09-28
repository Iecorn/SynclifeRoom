package com.synclife.studyroom.application.service;

import com.synclife.studyroom.domain.entity.User;
import com.synclife.studyroom.domain.repository.UserRepository;
import com.synclife.studyroom.global.exception.CustomException;
import com.synclife.studyroom.global.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.synclife.studyroom.global.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserReadService {

  private final UserRepository userRepository;
  private final TokenService tokenService;

  public String login(String id, String pw){
    log.info("로그인 시도 - loginId: {}", id);

    User user = userRepository.findByLoginIdAndLoginPw(id, pw)
            .orElseThrow(() -> {
              log.warn("로그인 실패 - 존재하지 않는 사용자 loginId: {}", id);
              return new CustomException(USER_NOT_FOUND);
            });

    String token = tokenService.generateToken(user);

    log.info("로그인 성공 - userId: {}, loginId: {}", user.getUserId(), user.getLoginId());

    return token;
  }
}
