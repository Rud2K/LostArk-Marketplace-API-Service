package com.lostark.marketplace.persist.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "point_history")
public class PointHistoryEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pointHistoryId; // 고유 식별자
  
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user; // 연관된 유저 정보
  
  private Integer points; // 적립한 포인트
  
  private String reason; // 포인트 적립 사유
  
  private LocalDateTime createdAt; // 포인트 적립일
  
}
