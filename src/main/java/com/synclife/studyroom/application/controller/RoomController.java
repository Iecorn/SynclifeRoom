package com.synclife.studyroom.application.controller;

import com.synclife.studyroom.application.dto.request.RoomRequest;
import com.synclife.studyroom.application.dto.response.RoomReservationResponse;
import com.synclife.studyroom.application.dto.response.RoomResponse;
import com.synclife.studyroom.application.service.RoomReadService;
import com.synclife.studyroom.application.service.RoomWriteService;
import com.synclife.studyroom.global.security.TokenService;
import com.synclife.studyroom.global.security.UserPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
  private final RoomReadService roomReadService;
  private final RoomWriteService roomWriteService;
  private final TokenService tokenService;

  @PostMapping
  public ResponseEntity<RoomResponse> createRoom(@RequestBody @Valid RoomRequest request,
                                                 @RequestHeader("Authorization") String tokenHeader) {
    UserPayload payload = tokenService.parseToken(tokenHeader.replace("Bearer ", ""));
    RoomResponse response = roomWriteService.createRoom(request, payload);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<List<RoomReservationResponse>> searchAvailReservationTime(
          @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    LocalTime startTime = LocalTime.of(0, 0);
    LocalTime endTime = LocalTime.of(23, 0);
    List<RoomReservationResponse> responses = roomReadService.calculateFreeTime(date, startTime, endTime);

    return ResponseEntity.status(HttpStatus.OK).body(responses);
  }

}
