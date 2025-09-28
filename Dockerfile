FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# netcat 설치 (Debian/Ubuntu 기반)
RUN apt-get update && apt-get install -y netcat dos2unix && rm -rf /var/lib/apt/lists/*

# jar 파일과 wait-for-it.sh 복사
COPY build/libs/studyroom-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh

# CRLF 문제 해결 & 실행 권한 부여
RUN dos2unix /wait-for-it.sh \
    && chmod +x /wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["/wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "app.jar"]
