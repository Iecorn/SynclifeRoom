package com.synclife.studyroom.application.controller;

import com.synclife.studyroom.application.dto.request.LoginRequest;
import com.synclife.studyroom.application.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserReadService userReadService;

  @PostMapping("/login")
  public Map<String, String> login(@RequestBody LoginRequest request){
    String token = userReadService.login(request.id(), request.pw());
    return Map.of("token", token);
  }
}
