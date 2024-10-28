package com.lostark.marketplace.service;

import java.util.List;
import com.lostark.marketplace.model.CharacterInfoDto;

public interface LostArkApiService {
  
  /**
   * 로스트아크 캐릭터 정보를 Open API로부터 불러오는 메서드.
   * 
   * @param characterName 불러올 캐릭터의 이름
   * @param currentRequestURI 현재 요청의 URI (에러 발생 시 로깅에 사용됨)
   * @return List<CharacterInfoDto> 캐릭터 정보 리스트
   * @throws LostarkMarketplaceException LostArk Open API가 사용 불가능하거나 오류 발생 시 던지는 예외
   */
  List<CharacterInfoDto> getCharacterData(String characterName, String currentRequestURI);
  
}
