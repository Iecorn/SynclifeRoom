package com.synclife.studyroom.global.exception.errorcode;

import org.springframework.http.HttpStatus;


public enum ReservationErrorCode implements ErrorCode {
  RESERVATION_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "RESERVATION_401", "잘못된 요청 권한입니다."),
  RESERVATION_TIME_RANGE_INVAILD(HttpStatus.BAD_REQUEST, "RESERVATION_400", "잘못된 시간 범위입니다."),
  RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION_404", "존재하지 않는 예약입니다."),
  RESERVATION_ALREADY_BOOKED(HttpStatus.CONFLICT, "RESERVATION_409", "이미 예약된 시간입니다.")
  ;

  private final HttpStatus status;
  private final String code;
  private final String message;

  ReservationErrorCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  @Override public HttpStatus getStatus() { return status; }
  @Override public String getCode() { return code; }
  @Override public String getMessage() { return message; }
}
