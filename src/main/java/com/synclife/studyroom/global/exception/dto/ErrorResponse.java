package com.synclife.studyroom.global.exception.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(
        String code,
        String message,
        int status,
        LocalDateTime timestamp,
        String path
) {
}
