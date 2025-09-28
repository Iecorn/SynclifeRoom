# 📚 API 문서

## 1. Users

### 1-1. 로그인

- **URL**: `/users/login`
- **Method**: `POST`
- **Headers**: 없음
- **Request Body**:

```json
{
  "id": "user01",
  "pw": "password123"
}

```

- **Response Body** (성공, 200 OK):

```json
{
  "token": "user-token-1"
}

```

- **설명**: 사용자 인증 후 JWT/토큰 발급. 이후 방/예약 API 호출 시 `Authorization: Bearer <token>` 헤더 필요.
- **예외**:
    - `USER_NOT_FOUND` (400 Bad Request) : 존재하지 않는 로그인 정보

---

## 2. Rooms

### 2-1. 방 생성 (ADMIN 전용)

- **URL**: `/rooms`
- **Method**: `POST`
- **Headers**:
    - `Authorization: Bearer <admin-token>`
- **Request Body**:

```json
{
  "userId": 1,
  "name": "회의실 A",
  "capacity": 10,
  "address": "서울 강남구 테헤란로 1길"
}

```

- **Response Body** (성공, 201 Created):

```json
{
  "roomId": 1,
  "userId": 1,
  "name": "회의실 A",
  "capacity": 10,
  "address": "서울 강남구 테헤란로 1길"
}

```

- **설명**:
    - 관리자만 방 생성 가능
    - 방 생성 시 요청자 권한 체크
- **예외**:
    - `ROOM_UNAUTHORIZED` (403 Forbidden) : ADMIN이 아닌 사용자가 방 생성 시도
    - `USER_NOT_FOUND` (400 Bad Request) : 존재하지 않는 userId

---

### 2-2. 가용 시간 조회

- **URL**: `/rooms`
- **Method**: `GET`
- **Query Parameter**:
    - `date` (필수) : 조회할 날짜 `YYYY-MM-DD`
- **Request Header**: 없음
- **Response Body** (성공, 200 OK):

```json
[
  {
    "roomId": 1,
    "name": "회의실 A",
    "times": [
      {"startAt": "2025-09-28T09:00:00", "endAt": "2025-09-28T10:00:00"},
      {"startAt": "2025-09-28T11:00:00", "endAt": "2025-09-28T12:00:00"}
    ]
  }
]

```

- **설명**: 해당 날짜 예약 현황 기준으로 방별 빈 시간 슬롯 제공.
- **예외**: 없음 (조회만 수행)

---

## 3. Reservations

### 3-1. 예약 생성

- **URL**: `/reservations`
- **Method**: `POST`
- **Headers**:
    - `Authorization: Bearer <user-token>`
- **Request Body**:

```json
{
  "userId": 1,
  "roomId": 1,
  "startAt": "2025-09-28T09:00:00+09:00",
  "endAt": "2025-09-28T10:00:00+09:00"
}

```

- **Response Body** (성공, 201 Created):

```json
{
  "reservationId": 1,
  "userId": 1,
  "roomId": 1,
  "startAt": "2025-09-28T09:00:00+09:00",
  "endAt": "2025-09-28T10:00:00+09:00",
  "createdAt": "2025-09-28T08:00:00+09:00"
}

```

- **설명**:
    - USER 권한만 예약 가능
    - 예약 시 동일 방 겹치는 시간대는 DB 제약(`EXCLUDE USING gist`) 또는 트랜잭션 처리로 방지
- **예외**:
    - `RESERVATION_UNAUTHORIZED` (403 Forbidden) : ADMIN 등 USER가 아닌 경우
    - `ROOM_NOT_FOUND` (400 Bad Request) : 존재하지 않는 roomId
    - `USER_NOT_FOUND` (400 Bad Request) : 존재하지 않는 userId
    - `RESERVATION_TIME_RANGE_INVAILD` (400 Bad Request) : startAt >= endAt
    - `RESERVATION_ALREADY_BOOKED` (409 Conflict) : 동일 시간대 이미 예약됨

---

### 3-2. 예약 취소

- **URL**: `/reservations/{reservationId}`
- **Method**: `DELETE`
- **Headers**:
    - `Authorization: Bearer <user-token>` (예약자 혹은 ADMIN만 가능)
- **Path Parameter**:
    - `reservationId` : 취소할 예약 ID
- **Response Body**: 없음 (204 No Content)
- **설명**:
    - 예약자 또는 ADMIN만 취소 가능
    - 취소 후 동일 구간 재예약 가능
- **예외**:
    - `RESERVATION_NOT_FOUND` (400 Bad Request) : 존재하지 않는 예약
    - `RESERVATION_UNAUTHORIZED` (403 Forbidden) : 권한 없는 사용자가 취소 시도
    - `USER_NOT_FOUND` (400 Bad Request) : 존재하지 않는 userId

---

### 3-3. 동시성 처리

- 예약 생성 시 DB 레벨에서 겹침 방지 제약을 통해 동시 요청에서도 무결성 보장
- 트랜잭션 처리로 예외 발생 시 롤백