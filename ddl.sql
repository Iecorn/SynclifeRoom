CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE IF NOT EXISTS "rooms" (
	"id" bigserial NOT NULL UNIQUE,
	"user_id" bigint NOT NULL,
	"name" varchar(255) NOT NULL,
	"capacity" int NOT NULL,
	"address" varchar(255) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "users" (
	"id" bigserial NOT NULL UNIQUE,
	"login_id" varchar(255) NOT NULL,
	"login_pw" varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	"role" varchar(255) NOT NULL DEFAULT '8',
	PRIMARY KEY ("id")
);

-- TODO btree_gist 확장 + EXCLUDE 제약, tstzrange 사용
CREATE TABLE IF NOT EXISTS "reservations" (
	"id" bigserial NOT NULL UNIQUE,
	"room_id" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"start_at" TIMESTAMPTZ  NOT NULL,
	"end_at" TIMESTAMPTZ  NOT NULL,
	"created_at" TIMESTAMPTZ  NOT NULL,
    EXCLUDE USING gist(
        room_id WITH =,
        tstzrange(start_at, end_at, '[)') WITH &&
        ),
	PRIMARY KEY ("id")
);

ALTER TABLE "rooms" ADD CONSTRAINT "rooms_fk1" FOREIGN KEY ("user_id") REFERENCES "users"("id");

ALTER TABLE "reservations" ADD CONSTRAINT "reservations_fk1" FOREIGN KEY ("room_id") REFERENCES "rooms"("id");

ALTER TABLE "reservations" ADD CONSTRAINT "reservations_fk2" FOREIGN KEY ("user_id") REFERENCES "users"("id");

SET TIME ZONE 'Asia/Seoul';