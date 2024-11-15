package com.lostark.marketplace.persist.entity;

import com.lostark.marketplace.model.CharacterInfoDto;
import com.lostark.marketplace.model.constant.LostArkClass;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Entity(name = "character_info")
public class CharacterInfoEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long characterId; // 고유 식별자 (자동 생성)
  
  private String serverName; // 서버 이름
  
  private String characterName; // 캐릭터 이름
  
  private Integer characterLevel; // 캐릭터 전투 레벨
  
  @Enumerated(EnumType.STRING)
  private LostArkClass characterClassName; // 캐릭터 직업
  
  private String itemAvgLevel; // 평균 아이템 레벨
  
  private String itemMaxLevel; // 최고 달성 아이템 레벨
  
  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user; // 해당 캐릭터와 연관된 유저 정보
  
  /**
   * CharacterInfoEntity를 CharacterInfoDto로 변환하는 메소드
   * 
   * @return CharacterInfoDto 객체
   */
  public CharacterInfoDto toDto() {
    return CharacterInfoDto.builder()
        .serverName(this.serverName)
        .characterName(this.characterName)
        .characterLevel(this.characterLevel)
        .characterClassName(this.characterClassName.getKoreanName())
        .itemAvgLevel(this.itemAvgLevel)
        .itemMaxLevel(this.itemMaxLevel)
        .build();
  }
  
}
