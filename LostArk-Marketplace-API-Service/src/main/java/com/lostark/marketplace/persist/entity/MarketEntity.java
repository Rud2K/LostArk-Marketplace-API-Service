package com.lostark.marketplace.persist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Entity(name = "market")
public class MarketEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long itemId; // 고유 식별자 (자동 생성)
  
  private String itemName; // 아이템 이름
  
  private String itemGrade; // 아이템 등급
  
  private Double yDayAvgPrice; // 어제 기준 평균 가격
  
  private Integer recentPrice; // 최근 판매 가격
  
  private Integer currentMinPrice; // 현재 최저 가격
  
}
