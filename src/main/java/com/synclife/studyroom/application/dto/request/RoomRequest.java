package com.synclife.studyroom.application.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RoomRequest(

        @NotNull(message = "사용자 ID는 필수입니다")
        Long userId,

        @NotBlank(message = "이름은 공백이거나 빈값이면 안됩니다.")
        String name,

        @Min(value = 1, message = "수용 인원은 최소 1명 이상이여야 합니다.")
        int capacity,

        @NotBlank(message = "주소는 공백이거나 빈값이면 안됩니다.")
        String address
) {
}
