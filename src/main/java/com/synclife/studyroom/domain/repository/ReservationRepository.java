package com.synclife.studyroom.domain.repository;

import com.synclife.studyroom.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


  long deleteByReservationId(Long reservationId);


}
