package com.synclife.studyroom.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long roomId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "name")
  private String name;

  @Column(name = "capacity")
  private int capacity;

  @Column(name = "address")
  private String address;

  @Builder
  public Room(User user, String name, int capacity, String address) {
    this.user = user;
    this.name = name;
    this.capacity = capacity;
    this.address = address;
  }
}


