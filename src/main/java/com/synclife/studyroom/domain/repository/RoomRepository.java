package com.synclife.studyroom.domain.repository;

import com.synclife.studyroom.domain.entity.Room;
import com.synclife.studyroom.domain.repository.custom.RoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {

}
