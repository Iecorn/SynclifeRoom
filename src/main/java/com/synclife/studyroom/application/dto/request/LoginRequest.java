package com.synclife.studyroom.application.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LoginRequest(
        @NotBlank(message = "ID은 공백이거나 빈값이면 안됩니다.")
        String id,
        @NotBlank(message = "PW은 공백이거나 빈값이면 안됩니다.")
        String pw
) {
}
