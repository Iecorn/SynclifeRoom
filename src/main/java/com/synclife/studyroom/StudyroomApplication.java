package com.synclife.studyroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyroomApplication {

  public static void main(String[] args) {
    SpringApplication.run(StudyroomApplication.class, args);
  }

}
