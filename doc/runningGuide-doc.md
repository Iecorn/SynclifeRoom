# ▶️ 실행 가이드

## 1. 프로젝트 다운로드

```bash
git clone <프로젝트 Git URL>
cd studyroom
```

---

## 2. Gradle 빌드

```bash
./gradlew clean build
```

---

## 3. Docker 환경 실행

```bash
docker-compose up --build
```

- PostgreSQL + Spring Boot 애플리케이션이 동시에 실행됩니다.
- 기본 포트: `localhost:8081`

---


## 4. 테스트 데이터 정보
- **사용자**

| 로그인 ID  | 비밀번호   | 이름  | 역할    |
| ------- | ------ | --- | ----- |
| admin01 | pw1234 | 관리자 | ADMIN |
| user01  | pw1111 | 김철수 | USER  |
| user02  | pw2222 | 이영희 | USER  |
| user03  | pw3333 | 박민수 | USER  |
| user04  | pw4444 | 최지현 | USER  |
| user05  | pw5555 | 한유리 | USER  |
- **룸**

| ID | 소유자 User ID | 이름 | 수용 인원 | 주소               |
| -- | ----------- | -- | ----- | ---------------- |
| 1  | 1           | A룸 | 10    | 서울시 강남구 테헤란로 123 |
| 2  | 1           | B룸 | 6     | 서울시 강남구 테헤란로 123 |
| 3  | 2           | C룸 | 4     | 서울시 서초구 반포대로 456 |
| 4  | 3           | D룸 | 8     | 서울시 송파구 올림픽로 789 |
| 5  | 4           | E룸 | 12    | 서울시 종로구 세종대로 101 |

- **예약 데이터**: 초기화 시 자동 생성, 예약 시간 `[startAt, endAt)` 반개구간 적용

---

## 5. Postman API 테스트 예제

### 5-1. 로그인 (토큰 발급)

- **Method**: POST
- **URL**: `http://localhost:8081/users/login`
- **Request Body (JSON)**:

```json
{
  "id": "admin01",
  "pw": "pw1234"
}
```

- **Response**:

```json
{
  "token": "<발급된 토큰>"
}
```

> 이후 요청 시 Header에 Authorization: Bearer <token> 추가 필수
>

---

### 5-2. 방 생성 (ADMIN 전용)

- **Method**: POST
- **URL**: `http://localhost:8081/rooms`
- **Headers**:

```
Authorization: Bearer <admin 토큰>
Content-Type: application/json
```

- **Request Body**:

```json
{
  "userId": 1,
  "name": "F룸",
  "capacity": 5,
  "address": "서울시 강남구 새주소 111"
}
```

- **Response (201 Created)**:

```json
{
  "roomId": 6,
  "userId": 1,
  "name": "F룸",
  "address": "서울시 강남구 새주소 111",
  "capacity": 5
}
```

---

### 5-3. 예약 가능 시간 조회

- **Method**: GET
- **URL**: `http://localhost:8081/rooms?date=2025-10-01`
- **Response (200 OK)**:

```json
[
  {
    "roomId": 1,
    "name": "A룸",
    "times": [
      {"startAt": "2025-10-01T00:00:00", "endAt": "2025-10-01T01:00:00"},
      {"startAt": "2025-10-01T01:00:00", "endAt": "2025-10-01T02:00:00"}
    ]
  }
]
```

---

### 5-4. 예약 생성 (USER 전용)

- **Method**: POST
- **URL**: `http://localhost:8081/reservations`
- **Headers**:

```
Authorization: Bearer <user 토큰>
Content-Type: application/json
```

- **Request Body**:

```json
{
  "userId": 2,
  "roomId": 1,
  "startAt": "2025-10-01T10:00:00",
  "endAt": "2025-10-01T12:00:00"
}
```

- **Response (201 Created)**:

```json
{
  "reservationId": 101,
  "userId": 2,
  "roomId": 1,
  "startAt": "2025-10-01T10:00:00",
  "endAt": "2025-10-01T12:00:00",
  "createdAt": "2025-09-28T16:00:00"
}
```

---

### 5-5. 예약 삭제 (OWNER 또는 ADMIN)

- **Method**: DELETE
- **URL**: `http://localhost:8081/reservations/101`
- **Headers**:

```
Authorization: Bearer <user 또는 admin 토큰>
```

- **Response (204 No Content)**: 정상 삭제

---

### ⚡ 참고 사항

1. Postman에서 반드시 **Bearer 토큰** 사용
2. 예약 시간 겹침 시 409 오류 발생
3. ADMIN만 방 생성 가능, USER는 자신 예약만 취소 가능
4. 테스트 데이터 초기화 후 바로 API 테스트 가능
5. 동시성 테스트는 Postman Collection Run에서 Iteration : 10, Delay : 0으로 테스트
6. 토큰은 Postman에서 Header에 키는 Key → Authorization, Value → 실제 토큰 문자열을 기입
