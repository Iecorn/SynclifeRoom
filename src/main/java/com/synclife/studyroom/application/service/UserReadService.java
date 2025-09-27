package com.synclife.studyroom.application.service;

import com.synclife.studyroom.domain.entity.User;
import com.synclife.studyroom.domain.repository.UserRepository;
import com.synclife.studyroom.global.exception.CustomException;
import com.synclife.studyroom.global.exception.errorcode.UserErrorCode;
import com.synclife.studyroom.global.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserReadService {

  private final UserRepository userRepository;
  private final TokenService tokenService;

  public String login(String id, String pw){
    User user = userRepository.findByLoginIdAndLoginPw(id, pw)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    return tokenService.generateToken(user);
  }
}
