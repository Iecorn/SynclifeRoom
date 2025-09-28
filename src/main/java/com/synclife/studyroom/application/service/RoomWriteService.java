package com.synclife.studyroom.application.service;

import com.synclife.studyroom.application.dto.request.RoomRequest;
import com.synclife.studyroom.application.dto.response.RoomResponse;
import com.synclife.studyroom.domain.entity.Room;
import com.synclife.studyroom.domain.entity.User;
import com.synclife.studyroom.domain.repository.RoomRepository;
import com.synclife.studyroom.domain.repository.UserRepository;
import com.synclife.studyroom.global.exception.CustomException;
import com.synclife.studyroom.global.security.UserPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.synclife.studyroom.global.exception.errorcode.RoomErrorCode.ROOM_UNAUTHORIZED;
import static com.synclife.studyroom.global.exception.errorcode.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoomWriteService {

  private final RoomRepository roomRepository;
  private final UserRepository userRepository;

  static final String ADMIN = "ADMIN";


  // 어드민 전용
  public RoomResponse createRoom(RoomRequest roomRequest, UserPayload payload){
    log.info("방 생성 요청 - 요청자 userId={}, role={}, roomName={}",
            payload.userId(), payload.role(), roomRequest.name());

    if(!payload.role().equals(ADMIN)){
      log.warn("방 생성 권한 없음 - 요청자 userId={}, role={}", payload.userId(), payload.role());
      throw new CustomException(ROOM_UNAUTHORIZED);
    }

    User user = userRepository.findById(roomRequest.userId())
            .orElseThrow(() -> {
              log.error("방 생성 실패 - 존재하지 않는 유저 userId={}", roomRequest.userId());
              return new CustomException(USER_NOT_FOUND);
            });

    Room room = Room.builder()
            .user(user)
            .name(roomRequest.name())
            .address(roomRequest.address())
            .capacity(roomRequest.capacity())
            .build();

    roomRepository.save(room);

    log.info("방 생성 성공 - roomId={}, roomName={}, ownerUserId={}",
            room.getRoomId(), room.getName(), user.getUserId());

    return RoomResponse.builder()
            .roomId(room.getRoomId())
            .userId(user.getUserId())
            .name(room.getName())
            .address(room.getAddress())
            .capacity(room.getCapacity())
            .build();

  }
}