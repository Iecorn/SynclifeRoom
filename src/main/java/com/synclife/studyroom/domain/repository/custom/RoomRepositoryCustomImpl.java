package com.synclife.studyroom.domain.repository.custom;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synclife.studyroom.application.dto.response.RoomReservationResponse;
import com.synclife.studyroom.application.dto.response.StartEndTime;
import com.synclife.studyroom.domain.entity.QReservation;
import com.synclife.studyroom.domain.entity.QRoom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RoomRepositoryCustomImpl implements RoomRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  @Override
  public List<RoomReservationResponse> findRoomsByDate(LocalDate targetDate) {
    QRoom room = QRoom.room;
    QReservation res = QReservation.reservation;

    // 조인 후 날짜로 검색
    List<Tuple> tuples = queryFactory
            .select(room.roomId, room.name, res.startAt, res.endAt)
            .from(room)
            .leftJoin(res).on(res.room.eq(room))
            .where(res.startAt.between(
                    targetDate.atStartOfDay(),
                    targetDate.plusDays(1).atStartOfDay()
            ))
            .orderBy(room.roomId.asc(), res.startAt.asc())
            .fetch();

    List<RoomReservationResponse> responses = new ArrayList<>();
    Long currentRoomId = null;
    RoomReservationResponse currentRoom = null;

    for (Tuple t : tuples) {
      Long roomId = t.get(room.roomId);

      // 새로운 room이면 새 DTO 생성
      if (!roomId.equals(currentRoomId)) {
        currentRoomId = roomId;
        currentRoom = new RoomReservationResponse(
                roomId,
                t.get(room.name),
                new ArrayList<>()
        );
        responses.add(currentRoom);
      }

      // 예약 시간 추가
      if (t.get(res.startAt) != null && t.get(res.endAt) != null) {
        currentRoom.times().add(
                new StartEndTime(t.get(res.startAt), t.get(res.endAt))
        );
      }
    }

    return responses;
  }
}
