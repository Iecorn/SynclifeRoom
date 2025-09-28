package com.synclife.studyroom.application.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RoomReservationResponse(
        Long roomId,
        String name,
        List<StartEndTime> times
) {
  @Builder
  @QueryProjection
  public RoomReservationResponse {}
}
