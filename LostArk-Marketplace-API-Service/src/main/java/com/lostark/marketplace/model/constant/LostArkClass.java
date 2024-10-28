package com.lostark.marketplace.model.constant;

import lombok.Getter;

/**
 * 로스트아크의 직업을 나타내는 enum 클래스입니다.
 */
@Getter
public enum LostArkClass {
  
  // 전사 계열
  BERSERKER("버서커"),
  DESTROYER("디스트로이어"),
  GUNLANCER("워로드"),
  PALADIN("홀리나이트"),
  SLAYER("슬레이어"),
  
  // 마법사 계열
  SORCERESS("소서리스"),
  SUMMONER("서머너"),
  ARCANA("아르카나"),
  BARD("바드"),
  
  // 헌터 계열
  DEADYE("데빌헌터"),
  ARTILLERIST("블래스터"),
  SHARPSHOOTER("호크아이"),
  MACHINIST("스카우터"),
  GUNSLINGER("건슬링어"),
  
  // 암살자 계열
  SHADOWHUNTER("데모닉"),
  REAPER("리퍼"),
  DEATHBLADE("블레이드"),
  SOULEATER("소울이터"),
  
  // 무도가 계열
  WARDANCER("배틀마스터"),
  SCRAPPER("인파이터"),
  SOULFIST("기공사"),
  GLAIVIER("창술사"),
  STRIKER("스트라이터"),
  BREAKER("브레이커");
  
  private final String koreanName;
  
  LostArkClass(String koreanName) {
    this.koreanName = koreanName;
  }
  
  /**
   * 한글 이름을 영어 Enum 값으로 변환하는 정적 메서드
   * 
   * @param koreanName 한글 이름
   * @return 매칭되는 LostArkClass Enum
   */
  public static LostArkClass fromKoreanName(String koreanName) {
    for (LostArkClass clazz : values()) {
      if (clazz.getKoreanName().equals(koreanName)) {
        return clazz;
      }
    }
    throw new IllegalArgumentException("No enum constant for Korean name: " + koreanName);
  }
  
}
