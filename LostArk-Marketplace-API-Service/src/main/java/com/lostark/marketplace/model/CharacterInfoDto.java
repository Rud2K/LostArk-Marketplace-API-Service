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
public class CharacterInfoDto {
  
  @JsonProperty("ServerName")
  private String serverName; // 서버 이름
  
  @JsonProperty("CharacterName")
  private String characterName; // 캐릭터 이름
  
  @JsonProperty("CharacterLevel")
  private Integer characterLevel; // 캐릭터 전투 레벨
  
  @JsonProperty("CharacterClassName")
  private String characterClassName; // 캐릭터 직업
  
  @JsonProperty("ItemAvgLevel")
  private String itemAvgLevel; // 평균 아이템 레벨
  
  @JsonProperty("ItemMaxLevel")
  private String itemMaxLevel; // 최고 달성 아이템 레벨
  
}
