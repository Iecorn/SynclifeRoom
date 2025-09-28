FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# netcat 설치 (Debian/Ubuntu 기반)
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

COPY build/libs/studyroom-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["/wait-for-it.sh", "postgres:5432", "--", "java", "-jar", "app.jar"]
