package com.synclife.studyroom;

import com.synclife.studyroom.domain.entity.Reservation;
import com.synclife.studyroom.domain.entity.Room;
import com.synclife.studyroom.domain.entity.User;
import com.synclife.studyroom.domain.entity.constant.Role;
import com.synclife.studyroom.domain.repository.ReservationRepository;
import com.synclife.studyroom.domain.repository.RoomRepository;
import com.synclife.studyroom.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.synclife.studyroom.domain.entity.constant.Role.*;

@Component
@RequiredArgsConstructor
public class TestDataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RoomRepository roomRepository;
  private final ReservationRepository reservationRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // User 생성
    User admin = User.builder().loginId("admin01").loginPw("pw1234").name("관리자").role(ADMIN).build();
    User user1 = User.builder().loginId("user01").loginPw("pw1111").name("김철수").role(USER).build();
    User user2 = User.builder().loginId("user02").loginPw("pw2222").name("이영희").role(USER).build();
    User user3 = User.builder().loginId("user03").loginPw("pw3333").name("박민수").role(USER).build();
    User user4 = User.builder().loginId("user04").loginPw("pw4444").name("최지현").role(USER).build();
    User user5 = User.builder().loginId("user05").loginPw("pw5555").name("한유리").role(USER).build();

    List<User> users = List.of(admin, user1, user2, user3, user4, user5);
    userRepository.saveAll(users);

    // Room 생성
    Room roomA = Room.builder().user(admin).name("A룸").capacity(10).address("서울시 강남구 테헤란로 123").build();
    Room roomB = Room.builder().user(admin).name("B룸").capacity(6).address("서울시 강남구 테헤란로 123").build();
    Room roomC = Room.builder().user(user1).name("C룸").capacity(4).address("서울시 서초구 반포대로 456").build();
    Room roomD = Room.builder().user(user2).name("D룸").capacity(8).address("서울시 송파구 올림픽로 789").build();
    Room roomE = Room.builder().user(user3).name("E룸").capacity(12).address("서울시 종로구 세종대로 101").build();

    List<Room> rooms = List.of(roomA, roomB, roomC, roomD, roomE);
    roomRepository.saveAll(rooms);

    LocalDateTime baseDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

    for (Room room : rooms) {
      for (int day = 3; day < 15; day++) {
        for (int i = 0; i < 4; i++) {
          LocalDateTime startTime = baseDate.plusDays(day).withHour(9 + i * 2).withMinute(0).withSecond(0).withNano(0);
          LocalDateTime endTime = startTime.plusHours(2);

          // 방장 제외, 한 유저만 선택
          List<User> availableUsers = users.stream()
                  .filter(u -> !u.getUserId().equals(room.getUser().getUserId()))
                  .toList();

          if (availableUsers.isEmpty()) continue;

          // 랜덤으로 한 명 선택
          User user = availableUsers.get((int)(Math.random() * availableUsers.size()));

          Reservation reservation = Reservation.builder()
                  .room(room)
                  .user(user)
                  .startAt(startTime)
                  .endAt(endTime)
                  .build();

          reservationRepository.save(reservation);
        }
      }
    }
  }
}