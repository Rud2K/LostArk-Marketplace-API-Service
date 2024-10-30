package com.lostark.marketplace.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDto {
  
  @JsonProperty("Sort")
  private String sort; // 정렬 기준
  
  @JsonProperty("CategoryCode")
  private Integer categoryCode; // 카테고리 코드
  
  @JsonProperty("CharacterClass")
  private String characterClass; // 캐릭터 클래스
  
  @JsonProperty("ItemTier")
  private Integer itemTier; // 아이템 티어
  
  @JsonProperty("ItemGrade")
  private String itemGrade; // 아이템 등급
  
  @JsonProperty("ItemName")
  private String itemName; // 아이템 이름
  
  @JsonProperty("PageNo")
  private Integer pageNo; // 페이지 번호
  
  @JsonProperty("SortCondition")
  private String sortCondition; // 정렬 조건
  
}
