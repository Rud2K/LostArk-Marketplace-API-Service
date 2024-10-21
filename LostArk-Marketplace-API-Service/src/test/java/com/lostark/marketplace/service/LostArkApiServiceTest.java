package com.lostark.marketplace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.lostark.marketplace.model.CharacterInfoDto;

@SpringBootTest
class LostArkApiServiceTest {
  
  @Autowired
  private LostArkApiService lostarkApiService;
  
  /**
   * API 호출 성공 케이스를 테스트하는 메서드입니다.
   * 캐릭터 정보를 성공적으로 받아올 경우, 파싱된 데이터가 반환되는지 확인합니다.
   */
  @Test
  @DisplayName("캐릭터 정보 API 실제 호출 테스트")
  void getCharacterInfo_실제_호출_테스트() {
    // Given: 테스트에 사용할 캐릭터 이름
    String characterName = "인파깽";
    
    // When: 실제 메서드를 호출하여 API로부터 데이터를 받음
    List<CharacterInfoDto> actualResponse = lostarkApiService.getCharacterInfo(characterName);
    
    // Then: 응답이 null이 아닌지 확인
    assertNotNull(actualResponse, "API 응답이 null이어서는 안됩니다.");
    assertTrue(!actualResponse.isEmpty(), "응답 데이터가 비어 있지 않아야 합니다.");
  }
  
}
