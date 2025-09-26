CREATE TABLE IF NOT EXISTS "rooms" (
	"id" serial NOT NULL UNIQUE,
	"user_id" bigint NOT NULL,
	"name" varchar(255) NOT NULL,
	"capacity" bigint NOT NULL,
	"addresss" varchar(255) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "users" (
	"id" serial NOT NULL UNIQUE,
	"login_id" varchar(255) NOT NULL,
	"login_pw" varchar(255) NOT NULL,
	"name" varchar(255) NOT NULL,
	"role" varchar(255) NOT NULL DEFAULT '8',
	PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "reservations" (
	"id" serial NOT NULL UNIQUE,
	"room_id" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"start_at" varchar(255) NOT NULL,
	"end_at" varchar(255) NOT NULL,
	"created_at" varchar(255) NOT NULL,
	PRIMARY KEY ("id")
);

ALTER TABLE "rooms" ADD CONSTRAINT "rooms_fk1" FOREIGN KEY ("user_id") REFERENCES "users"("id");

ALTER TABLE "reservations" ADD CONSTRAINT "reservations_fk1" FOREIGN KEY ("room_id") REFERENCES "rooms"("id");

ALTER TABLE "reservations" ADD CONSTRAINT "reservations_fk2" FOREIGN KEY ("user_id") REFERENCES "users"("id");