package com.synclife.studyroom.global.security;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserPayload(
        @NotNull(message = "사용자 ID는 필수입니다")
        Long userId,
        @NotBlank(message = "역할은 공백이거나 빈값이면 안됩니다.")
        String role
) {
}
