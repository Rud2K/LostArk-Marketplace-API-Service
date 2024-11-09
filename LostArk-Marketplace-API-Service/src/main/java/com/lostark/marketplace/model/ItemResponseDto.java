package com.lostark.marketplace.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({
  "id",
  "name",
  "grade",
  "icon",
  "bundleCount",
  "tradeRemainCount",
  "yDayAvgPrice",
  "recentPrice",
  "currentMinPrice",
  "itemType"
})
public class ItemResponseDto {
  
  @JsonProperty("Id")
  private Long id; // 아이템 고유 식별자
  
  @JsonProperty("Name")
  private String name; // 아이템 이름
  
  @JsonProperty("Grade")
  private String grade; // 아이템 등급
  
  @JsonProperty("Icon")
  private String icon; // 아이템 이미지 파일 경로
  
  @JsonProperty("BundleCount")
  private Integer bundleCount; // 판매시 묶음 단위
  
  @JsonProperty("TradeRemainCount")
  private Integer tradeRemainCount; // 거래 가능 횟수 (nullable)
  
  @JsonProperty("YDayAvgPrice")
  private Double yDayAvgPrice; // 어제 기준 평균 가격
  
  @JsonProperty("RecentPrice")
  private Integer recentPrice; // 최근 판매 가격
  
  @JsonProperty("CurrentMinPrice")
  private Integer currentMinPrice; // 현재 최저 가격
  
  @Builder.Default
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String itemType = "기타"; // 아이템 유형
  
}
