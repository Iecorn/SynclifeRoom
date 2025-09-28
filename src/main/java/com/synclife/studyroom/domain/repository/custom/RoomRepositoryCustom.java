package com.synclife.studyroom.domain.repository.custom;

import com.synclife.studyroom.application.dto.response.RoomReservationResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepositoryCustom {
  List<RoomReservationResponse> findRoomsByDate(LocalDate date);
}
