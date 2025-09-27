package com.synclife.studyroom.global.exception.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  HttpStatus getStatus();

  String getCode();

  String getMessage();

}
