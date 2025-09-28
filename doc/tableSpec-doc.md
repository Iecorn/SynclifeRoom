# **테이블 명세서**

---

## **Users 테이블** (`users`)

**스키마 설명**

- 시스템 사용자 정보를 저장하는 테이블
- 로그인 정보, 이름, 역할(권한) 관리

| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PRIMARY KEY, UNIQUE, NOT NULL | 사용자 고유 ID |
| login_id | VARCHAR(255) | NOT NULL | 로그인 ID |
| login_pw | VARCHAR(255) | NOT NULL | 비밀번호 |
| name | VARCHAR(255) | NOT NULL | 사용자 이름 |
| role | VARCHAR(255) | NOT NULL, DEFAULT '8' | 사용자 권한/역할 |

---

## **Rooms 테이블** (`rooms`)

**스키마 설명**

- 예약 가능한 방(Room) 정보를 저장하는 테이블
- 방 이름, 수용 인원, 주소, 생성자 정보 관리

| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PRIMARY KEY, UNIQUE, NOT NULL | 방 고유 ID |
| user_id | BIGINT | NOT NULL, FK → users(id) | 방 생성자 (사용자) ID |
| name | VARCHAR(255) | NOT NULL | 방 이름 |
| capacity | INT | NOT NULL | 최대 수용 인원 |
| address | VARCHAR(255) | NOT NULL | 방 주소 |

**FK 관계**

- `user_id` → `users.id`

---

## **Reservations 테이블** (`reservations`)

**스키마 설명**

- 사용자 예약 정보를 저장하는 테이블
- 예약한 방, 예약자, 예약 시작/종료 시간, 생성 시간 관리
- 동일 방에서 예약 시간 중복 방지를 위해 EXCLUDE 제약 사용

| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PRIMARY KEY, UNIQUE, NOT NULL | 예약 고유 ID |
| room_id | BIGINT | NOT NULL, FK → rooms(id) | 예약한 방 ID |
| user_id | BIGINT | NOT NULL, FK → users(id) | 예약자 ID |
| start_at | TIMESTAMPTZ | NOT NULL | 예약 시작 시간 |
| end_at | TIMESTAMPTZ | NOT NULL | 예약 종료 시간 |
| created_at | TIMESTAMPTZ | NOT NULL | 예약 생성 시간 |

**제약 조건**

- **시간 중복 방지**: `EXCLUDE USING gist(room_id WITH =, tstzrange(start_at, end_at, '[)') WITH &&)`

  → 동일 방에서 예약 시간 범위가 겹치지 않도록 함.


**FK 관계**

- `room_id` → `rooms.id`
- `user_id` → `users.id`

---

## **설정**

- 타임존: `Asia/Seoul`
- Postgres extension: `btree_gist` (EXCLUDE 제약 사용 위해 필요)