package com.synclife.studyroom.application.service;

import com.synclife.studyroom.application.dto.request.RoomRequest;
import com.synclife.studyroom.application.dto.response.RoomResponse;
import com.synclife.studyroom.domain.entity.Room;
import com.synclife.studyroom.domain.entity.User;
import com.synclife.studyroom.domain.repository.RoomRepository;
import com.synclife.studyroom.domain.repository.UserRepository;
import com.synclife.studyroom.global.exception.CustomException;
import com.synclife.studyroom.global.exception.errorcode.RoomErrorCode;
import com.synclife.studyroom.global.exception.errorcode.UserErrorCode;
import com.synclife.studyroom.global.security.UserPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    if(!payload.role().equals(ADMIN)){
      throw new CustomException(RoomErrorCode.ROOM_UNAUTHORIZED);
    }

    User user = userRepository.findById(roomRequest.userId())
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

    Room room = Room.builder()
            .user(user)
            .name(roomRequest.name())
            .address(roomRequest.address())
            .capacity(roomRequest.capacity())
            .build();

    roomRepository.save(room);

    log.info("Room created: {}", room.getName());

    return RoomResponse.builder()
            .roomId(room.getRoomId())
            .userId(user.getUserId())
            .name(room.getName())
            .address(room.getAddress())
            .capacity(room.getCapacity())
            .build();
  }
}