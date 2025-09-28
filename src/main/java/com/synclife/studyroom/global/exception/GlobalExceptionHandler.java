package com.synclife.studyroom.global.exception;

import com.synclife.studyroom.global.exception.dto.ErrorResponse;
import com.synclife.studyroom.global.exception.errorcode.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
    ErrorCode code = ex.getErrorCode();

    return ResponseEntity
            .status(code.getStatus())
            .body(
                    ErrorResponse.builder()
                            .code(code.getCode())
                            .message(code.getMessage())
                            .status(code.getStatus().value())
                            .timestamp(LocalDateTime.now())
                            .path(request.getRequestURI())
                            .build()
            );
  }
}
