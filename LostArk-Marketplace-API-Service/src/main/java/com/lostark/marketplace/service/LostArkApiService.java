package com.lostark.marketplace.service;

import java.util.List;
import com.lostark.marketplace.model.CharacterInfoDto;
import com.lostark.marketplace.model.MarketResponseDto;

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
  
  /**
   * 5분마다 마켓 정보를 가져와 DB에 저장하는 메서드
   */
  void updateMarketData();
  
  /**
   * 로스트아크 거래소 데이터를 Open API로부터 가져오는 메서드.
   * 
   * @param currentRequestURI 현재 요청의 URI (에러 발생 시 로깅에 사용됨)
   * @return List<MarketResponseDto> 거래소 아이템 데이터 리스트
   * @throws LostarkMarketplaceException LostArk API가 사용 불가능하거나 오류 발생 시 던지는 예외
   */
  List<MarketResponseDto> getMarketData(String currentRequestURI);
  
}
