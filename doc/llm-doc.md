# LLM 사용 구간

## 1. Docker & Docker-Compose 배포 환경 구축

### 상황

Docker에 대한 사전 지식이 부족한 상태에서 Spring Boot + PostgreSQL 환경을 컨테이너화해야 했음

### LLM 활용

- docker-compose.yml 파일 구조 및 설정 방법 학습
- 각 설정 옵션의 의미와 용도 이해
- 볼륨 마운트, 환경변수, 네트워크 설정 구성

---

## 2. QueryDSL 쿼리 작성

### 상황

복잡한 JOIN과 날짜 조건이 포함된 QueryDSL 쿼리 작성 필요

java

`List<Tuple> tuples = queryFactory
.select(room.roomId, room.name, res.startAt, res.endAt)
.from(room)
.leftJoin(res).on(res.room.eq(room))
.where(res.startAt.between(
targetDate.atStartOfDay(),
targetDate.plusDays(1).atStartOfDay()
))
.orderBy(room.roomId.asc(), res.startAt.asc())
.fetch();`

### LLM 활용

1. 먼저 Native SQL 쿼리로 직접 로직 구성
2. LLM을 사용하여 작성된 SQL을 QueryDSL 문법으로 변환
3. 날짜 비교 조건 및 JOIN 문법 확인

---

## 3. 에러 처리 방식 개선

### 상황

기존 프로젝트의 산발적이고 일관성 없는 예외 처리 방식을 개선하고자 함

### LLM 활용

- Spring Boot의 전역 예외 처리 모범 사례 학습
- @ControllerAdvice 활용 방법 습득
- 일관된 에러 응답 형식 설계

---

## 4. PostgreSQL 학습

### 4.1 기본 제약조건 및 기능

PostgreSQL의 주요 제약조건(PRIMARY KEY, FOREIGN KEY, UNIQUE, CHECK 등)과 활용 방법 학습

### 4.2 락(Lock) 메커니즘

PostgreSQL에서 제공하는 다양한 락의 종류와 동시성 제어 방법 이해

### 4.3 날짜 자료형 및 연산

DATE, TIMESTAMP, TIMESTAMPTZ의 차이점과 Java와의 매핑 관계 학습

### 4.4 GiST 인덱스

GiST(Generalized Search Tree) 인덱스의 구조와 B-Tree와의 차이점 이해

### 4.5 엔진 구조

PostgreSQL의 프로세스 구조, 메모리 관리, WAL 메커니즘 등 내부 아키텍처 학습