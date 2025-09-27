package com.synclife.studyroom.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "존재하지 않는 유저입니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;

  UserErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  @Override public HttpStatus getStatus() { return status; }
  @Override public String getCode() { return code; }
  @Override public String getMessage() { return message; }
}
