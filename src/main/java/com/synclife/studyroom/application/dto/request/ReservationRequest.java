package com.synclife.studyroom.application.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReservationRequest(
        @NotNull(message = "방 ID는 필수입니다")
        Long roomId,
        @NotNull(message = "사용자 ID는 필수입니다")
        Long userId,

        @FutureOrPresent(message = "예약 시작 시간은 현재 이후여야 합니다")
        LocalDateTime startAt,

        @Future(message = "예약 시작 시간은 현재 이후여야 합니다")
        LocalDateTime endAt
) {
}
