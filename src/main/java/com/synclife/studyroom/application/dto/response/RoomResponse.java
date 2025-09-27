package com.synclife.studyroom.application.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RoomResponse(

        Long roomId,
        Long userId,
        String name,
        int capacity,
        String address
) {
}
