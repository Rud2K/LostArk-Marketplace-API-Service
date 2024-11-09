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
  
  private String icon; // 아이템 아이콘 이미지 파일 경로
  
  private Integer bundleCount; // 판매 시 묶음 단위
  
  private Integer tradeRemainCount; // 거래 가능 횟수
  
  private Double yDayAvgPrice; // 어제 기준 평균 가격
  
  private Integer recentPrice; // 최근 판매 가격
  
  private Integer currentMinPrice; // 현재 최저 가격
  
  private String itemType; // 아이템 유형 (예: 강화 재료, 각인서 등)
  
}
