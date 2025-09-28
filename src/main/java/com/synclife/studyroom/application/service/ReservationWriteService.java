package com.synclife.studyroom.application.service;

import com.synclife.studyroom.application.dto.request.ReservationRequest;
import com.synclife.studyroom.application.dto.response.ReservationResponse;
import com.synclife.studyroom.domain.entity.Reservation;
import com.synclife.studyroom.domain.entity.Room;
import com.synclife.studyroom.domain.entity.User;
import com.synclife.studyroom.domain.repository.ReservationRepository;
import com.synclife.studyroom.domain.repository.RoomRepository;
import com.synclife.studyroom.domain.repository.UserRepository;
import com.synclife.studyroom.global.exception.CustomException;
import com.synclife.studyroom.global.exception.errorcode.ReservationErrorCode;
import com.synclife.studyroom.global.security.UserPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.synclife.studyroom.global.exception.errorcode.ReservationErrorCode.*;
import static com.synclife.studyroom.global.exception.errorcode.RoomErrorCode.ROOM_NOT_FOUND;
import static com.synclife.studyroom.global.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationWriteService {

  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  static final String USER = "USER";

  // 유저만 예약 가능
  public ReservationResponse createReservation(ReservationRequest reservationRequest, UserPayload payload) {

    log.info("예약 생성 요청 - userId={}, roomId={}, startAt={}, endAt={}, role={}",
            reservationRequest.userId(), reservationRequest.roomId(),
            reservationRequest.startAt(), reservationRequest.endAt(), payload.role());

    if (!payload.role().equals(USER)) {
      log.warn("예약 권한 없음 - userId={}, role={}", payload.userId(), payload.role());
      throw new CustomException(RESERVATION_UNAUTHORIZED);
    }

    Room room = roomRepository.findById(reservationRequest.roomId())
            .orElseThrow(() -> {
              log.error("예약 실패 - 존재하지 않는 방 roomId={}", reservationRequest.roomId());
              return new CustomException(ROOM_NOT_FOUND);
            });

    User user = userRepository.findById(reservationRequest.userId())
            .orElseThrow(() -> {
              log.error("예약 실패 - 존재하지 않는 유저 userId={}", reservationRequest.userId());
              return new CustomException(USER_NOT_FOUND);
            });

    if (reservationRequest.startAt().isAfter(reservationRequest.endAt())) {
      log.warn("예약 시간 범위 오류 - userId={}, roomId={}, startAt={}, endAt={}",
              user.getUserId(), room.getRoomId(),
              reservationRequest.startAt(), reservationRequest.endAt());
      throw new CustomException(RESERVATION_TIME_RANGE_INVAILD);
    }

    try {
      Reservation reservation = Reservation.builder()
              .room(room)
              .user(user)
              .startAt(reservationRequest.startAt())
              .endAt(reservationRequest.endAt())
              .build();
      reservationRepository.save(reservation);

      log.info("예약 성공 - reservationId={}, userId={}, roomId={}, startAt={}, endAt={}",
              reservation.getReservationId(), user.getUserId(), room.getRoomId(),
              reservation.getStartAt(), reservation.getEndAt());

      return ReservationResponse.builder()
              .reservationId(reservation.getReservationId())
              .userId(user.getUserId())
              .roomId(room.getRoomId())
              .startAt(reservation.getStartAt())
              .endAt(reservation.getEndAt())
              .createdAt(reservation.getCreatedAt())
              .build();
    } catch (DataIntegrityViolationException e) {
      log.error("예약 실패 - 이미 예약된 시간대 userId={}, roomId={}, startAt={}, endAt={}",
              user.getUserId(), room.getRoomId(),
              reservationRequest.startAt(), reservationRequest.endAt(), e);
      throw new CustomException(RESERVATION_ALREADY_BOOKED);
    }
  }



  // 어드민 및 예약한 사용자
  public void deleteReservation(Long reservationId, UserPayload payload) {
    log.info("예약 삭제 요청 - reservationId={}, userId={}, role={}",
            reservationId, payload.userId(), payload.role());

    User user = userRepository.findById(payload.userId())
            .orElseThrow(() -> {
              log.error("예약 삭제 실패 - 존재하지 않는 유저 userId={}", payload.userId());
              return new CustomException(USER_NOT_FOUND);
            });

    Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> {
              log.error("예약 삭제 실패 - 존재하지 않는 예약 reservationId={}", reservationId);
              return new CustomException(RESERVATION_NOT_FOUND);
            });

    boolean isAdmin = payload.role().equals("ADMIN");
    boolean isOwner = reservation.getUser().getUserId().equals(user.getUserId());

    if (!isAdmin && !isOwner) {
      log.warn("예약 삭제 권한 없음 - reservationId={}, userId={}, role={}",
              reservationId, payload.userId(), payload.role());
      throw new CustomException(ReservationErrorCode.RESERVATION_UNAUTHORIZED);
    }

    reservationRepository.delete(reservation);
    log.info("예약 삭제 성공 - reservationId={}, deletedByUserId={}", reservationId, payload.userId());
  }
}
