package com.synclife.studyroom.application.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StartEndTime(

        LocalDateTime startAt,

        LocalDateTime endAt
) {
  @QueryProjection
  public StartEndTime {
  }
}
