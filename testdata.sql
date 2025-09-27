INSERT INTO "users" (login_id, login_pw, name, role)
VALUES ('admin01', 'pw1234', '관리자', 'ADMIN'),
       ('user01', 'pw1111', '김철수', 'USER'),
       ('user02', 'pw2222', '이영희', 'USER'),
       ('user03', 'pw3333', '박민수', 'USER'),
       ('user04', 'pw4444', '최지현', 'USER'),
       ('user05', 'pw5555', '한유리', 'USER');

INSERT INTO "rooms" (user_id, name, capacity, address)
VALUES (1, 'A룸', 10, '서울시 강남구 테헤란로 123'),
       (1, 'B룸', 6, '서울시 강남구 테헤란로 123'),
       (2, 'C룸', 4, '서울시 서초구 반포대로 456'),
       (3, 'D룸', 8, '서울시 송파구 올림픽로 789'),
       (4, 'E룸', 12, '서울시 종로구 세종대로 101');

DO
$$
    DECLARE
        u_id       bigint;
        r_id       bigint;
        start_time timestamptz;
    BEGIN
        FOR r_id IN 1..5 LOOP  -- room_id 1~5
        FOR u_id IN 2..6 LOOP  -- user_id 2~6
        FOR i IN 0..3 LOOP  -- 하루 최대 4개 예약
        -- KST 기준 시간 계산
                start_time := (DATE '2025-10-01'
                    + (i * interval '2 hours')
                    + (u_id * interval '1 day'))
                    AT TIME ZONE 'Asia/Seoul';

                INSERT INTO reservations (room_id, user_id, start_at, end_at, created_at)
                VALUES (
                           r_id,
                           u_id,
                           start_time,
                           start_time + interval '2 hours',
                           now() AT TIME ZONE 'Asia/Seoul'  -- created_at도 KST 기준
                       );
            END LOOP;
            END LOOP;
            END LOOP;
    END
$$;