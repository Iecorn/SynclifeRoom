package com.synclife.studyroom.domain.entity;

import com.synclife.studyroom.domain.entity.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long userId;

  @Column(name = "login_id")
  private String loginId;

  @Column(name = "login_pw")
  private String loginPw;

  @Column(name = "name")
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;
}
