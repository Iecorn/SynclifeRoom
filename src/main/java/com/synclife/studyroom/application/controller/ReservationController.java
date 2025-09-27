package com.synclife.studyroom.application.controller;

import com.synclife.studyroom.application.dto.request.ReservationRequest;
import com.synclife.studyroom.application.dto.response.ReservationResponse;
import com.synclife.studyroom.application.service.ReservationWriteService;
import com.synclife.studyroom.global.security.TokenService;
import com.synclife.studyroom.global.security.UserPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {
  private final ReservationWriteService reservationWriteService;
  private final TokenService tokenService;

  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(@RequestBody @Valid ReservationRequest request,
                                                               @RequestHeader("Authorization") String tokenHeader) {
    UserPayload payload = tokenService.parseToken(tokenHeader.replace("Bearer ", ""));
    ReservationResponse response = reservationWriteService.createReservation(request, payload);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/{reservationId}")
  public ResponseEntity<Void> deleteById(@PathVariable Long reservationId,
                                         @RequestHeader("Authorization") String tokenHeader) {
    UserPayload payload = tokenService.parseToken(tokenHeader.replace("Bearer ", ""));
    reservationWriteService.deleteReservation(reservationId, payload);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
