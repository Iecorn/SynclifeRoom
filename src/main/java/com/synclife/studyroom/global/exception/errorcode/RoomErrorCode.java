package com.synclife.studyroom.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum RoomErrorCode implements ErrorCode {
  ROOM_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ROOM_401", "잘못된 요청 권한입니다."),
  ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ROOM_404", "존재하지 않는 방입니다."),
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;


  RoomErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  @Override public HttpStatus getStatus() { return status; }
  @Override public String getCode() { return code; }
  @Override public String getMessage() { return message; }
}
