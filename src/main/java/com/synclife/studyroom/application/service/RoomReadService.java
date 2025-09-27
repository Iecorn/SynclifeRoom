package com.synclife.studyroom.application.service;

import com.synclife.studyroom.application.dto.response.RoomReservationResponse;
import com.synclife.studyroom.application.dto.response.StartEndTime;
import com.synclife.studyroom.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoomReadService {

  private final RoomRepository roomRepository;

  public List<RoomReservationResponse> calculateFreeTime(LocalDate dateTime, LocalTime startTime, LocalTime endTime) {
    List<RoomReservationResponse> queryResults = roomRepository.findRoomsByDate(dateTime);
    log.debug("조회된 예약 결과 수: {}", queryResults.size());

    // 결과
    List<RoomReservationResponse> freeTimeRooms = new ArrayList<>();

    // 비는 시간 계산 시작
    for (RoomReservationResponse currentReservation: queryResults) {
      List<StartEndTime> reservations = currentReservation.times();
      log.debug("roomId: {}, roomName: {}, 예약 건수: {}", currentReservation.roomId(), currentReservation.name(), reservations.size());

      List<StartEndTime> freeTime = new ArrayList<>();

      //시작 시간 설정
      LocalTime current = startTime;
      // 시작 시간 ~ 예약이 존재하는 시간까지 계산
      for (StartEndTime res : reservations) {
        while (current.isBefore(res.startAt().toLocalTime())) {
          LocalTime end = current.plusHours(1);
          if (end.isAfter(res.startAt().toLocalTime()))
            end = res.startAt().toLocalTime();
          freeTime.add(new StartEndTime(LocalDateTime.of(dateTime, current), LocalDateTime.of(dateTime, end)));
          current = end;
        }

        // 다음 예약 건으로 넘어감
        current = res.endAt().toLocalTime();
      }

      // 마지막 예약 시간 이후 남은 시간 계산
      while (current.isAfter(endTime)) {
        LocalTime end = current.plusHours(1);
        if (end.isAfter(endTime))
          end = endTime;
        freeTime.add(new StartEndTime(LocalDateTime.of(dateTime, current), LocalDateTime.of(dateTime, end)));
      }


      // 요청 결과로 조립
      freeTimeRooms.add(RoomReservationResponse.builder()
              .roomId(currentReservation.roomId())
              .name(currentReservation.name())
              .times(freeTime)
              .build());
    }

    log.info("빈 시간 계산 완료 - 총 {}개의 룸 처리", freeTimeRooms.size());

    return freeTimeRooms;
  }
}
