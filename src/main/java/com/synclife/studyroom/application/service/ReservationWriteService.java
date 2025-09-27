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
import com.synclife.studyroom.global.exception.errorcode.RoomErrorCode;
import com.synclife.studyroom.global.exception.errorcode.UserErrorCode;
import com.synclife.studyroom.global.security.UserPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationWriteService {

  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  static final String USER = "USER";
  static final String ADMIN = "ADMIN";

  // 유저만 예약 가능
  public ReservationResponse createReservation(ReservationRequest reservationRequest, UserPayload payload){

    if(!payload.role().equals(USER)){
      throw new CustomException(ReservationErrorCode.RESERVATION_UNAUTHORIZED);
    }

    Room room = roomRepository.findById(reservationRequest.roomId())
            .orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));
    User user = userRepository.findById(reservationRequest.userId())
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    if(reservationRequest.startAt().isAfter(reservationRequest.endAt()))
      throw new CustomException(ReservationErrorCode.RESERVATION_TIME_RANGE_INVAILD);

    try {
      Reservation reservation = Reservation.builder()
              .room(room)
              .user(user)
              .startAt(reservationRequest.startAt())
              .endAt(reservationRequest.endAt())
              .build();
      reservationRepository.save(reservation);

      return ReservationResponse.builder()
              .reservationId(reservation.getReservationId())
              .userId(user.getUserId())
              .roomId(room.getRoomId())
              .startAt(reservation.getStartAt())
              .endAt(reservation.getEndAt())
              .createdAt(reservation.getCreatedAt())
              .build();
    } catch (DataIntegrityViolationException e){
      throw new CustomException(ReservationErrorCode.RESERVATION_ALREADY_BOOKED);
    }
  }


  // 어드민 및 예약한 사용자
  public void deleteReservation(Long reservationId, UserPayload payload) {
    User user = userRepository.findById(payload.userId())
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new CustomException(ReservationErrorCode.RESERVATION_NOT_FOUND));

    boolean isAdmin = payload.role().equals("ADMIN");
    boolean isOwner = reservation.getUser().getUserId().equals(payload.userId());

    if (!isAdmin && !isOwner) {
      throw new CustomException(ReservationErrorCode.RESERVATION_UNAUTHORIZED);
    }

    reservationRepository.delete(reservation);
    log.info("Reservation Deleted: reservation_id = {}", reservationId);
  }
}
