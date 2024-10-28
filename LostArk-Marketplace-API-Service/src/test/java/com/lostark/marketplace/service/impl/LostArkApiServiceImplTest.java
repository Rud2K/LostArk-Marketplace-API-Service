package com.lostark.marketplace.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.model.CharacterInfoDto;
import com.lostark.marketplace.model.ItemResponseDto;
import com.lostark.marketplace.model.MarketResponseDto;
import com.lostark.marketplace.persist.MarketRepository;

class LostArkApiServiceImplTest {
  
  @Mock
  private RestTemplate restTemplate;
  
  @Mock
  private MarketRepository marketRepository;
  
  @InjectMocks
  private LostArkApiServiceImpl lostArkApiServiceImpl;
  
  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
      registry.add("lostark.api-key", () -> "testApiKey");
      registry.add("lostark.base-url", () -> "https://developer-lostark.game.onstove.com");
  }
  
  private final ObjectMapper objectMapper = new ObjectMapper();
  
  private String testCharacterName;
  private String testCurrentRequestURI;
  
  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    
    // 테스트 캐릭터 이름 값 설정
    testCharacterName = "인파깽";
    
    // 테스트 응답 URI 설정
    testCurrentRequestURI = "/testURI";
  }
  
  @Test
  @DisplayName("getCharacterData() 성공 케이스 - 캐릭터 정보 호출 성공")
  void getCharacterData_Success() throws Exception {
    List<CharacterInfoDto> mockCharacterInfoList = List.of(
        new CharacterInfoDto("니나브", "인파깽", 60, "인파이터", "1,674.17", "1,674.17")
    );
    String mockJsonResponse = this.objectMapper.writeValueAsString(mockCharacterInfoList);
    
    when(this.restTemplate.exchange(
        anyString(),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        eq(String.class))
    ).thenReturn(ResponseEntity.ok(mockJsonResponse));
    
    List<CharacterInfoDto> result = this.lostArkApiServiceImpl.getCharacterData(testCharacterName, testCurrentRequestURI);
    
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("인파깽", result.get(0).getCharacterName());
    assertEquals("SCRAPPER", result.get(0).getCharacterClassName());
  }
  
  @Test
  @DisplayName("getCharacterData() 실패 케이스 - API 호출 실패")
  void getCharacterData_Failure() throws Exception {
    when(this.restTemplate.exchange(
        anyString(),
        eq(HttpMethod.GET),
        any(HttpEntity.class),
        eq(String.class))
    ).thenThrow(new RuntimeException("API 호출 실패"));
    
    LostArkMarketplaceException exception = assertThrows(
        LostArkMarketplaceException.class,
        () -> this.lostArkApiServiceImpl.getCharacterData(testCharacterName, testCurrentRequestURI));
    
    assertEquals(503, exception.getStatus());
    assertEquals(testCurrentRequestURI, exception.getPath());
  }
  
  @Test
  @DisplayName("getMarketData() 성공 케이스 - 마켓 데이터 호출 성공")
  void getMarketData_Success() throws Exception {
    MarketResponseDto mockMarketResponse = MarketResponseDto.builder()
        .pageSize(1)
        .items(List.of(ItemResponseDto.builder()
            .id(66102003L)
            .name("파괴석 결정")
            .grade("일반")
            .icon("https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_6_105.png")
            .bundleCount(10)
            .tradeRemainCount(null)
            .yDayAvgPrice(1.0)
            .recentPrice(1)
            .currentMinPrice(1)
            .build()))
        .build();
    
    String mockJsonResponse = this.objectMapper.writeValueAsString(mockMarketResponse);
    
    when(this.restTemplate.exchange(
        anyString(),
        eq(HttpMethod.POST),
        any(HttpEntity.class),
        eq(String.class))
    ).thenReturn(ResponseEntity.ok(mockJsonResponse));
    
    List<MarketResponseDto> result = this.lostArkApiServiceImpl.getMarketData(testCurrentRequestURI);
    
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("파괴석 결정", result.get(0).getItems().get(0).getName());
    assertEquals("일반", result.get(0).getItems().get(0).getGrade());
    assertEquals("https://cdn-lostark.game.onstove.com/efui_iconatlas/use/use_6_105.png", result.get(0).getItems().get(0).getIcon());
    assertEquals(1.0, result.get(0).getItems().get(0).getYDayAvgPrice());
    assertEquals(1, result.get(0).getItems().get(0).getRecentPrice());
    assertEquals(1, result.get(0).getItems().get(0).getCurrentMinPrice());
  }
  
  @Test
  @DisplayName("getMarketData() 실패 케이스 - API 호출 실패")
  void getMarketData_ApiCallFailure() {
    when(this.restTemplate.exchange(
        anyString(),
        eq(HttpMethod.POST),
        any(HttpEntity.class),
        eq(String.class)))
    .thenThrow(new RuntimeException("API 호출 실패"));
    
    LostArkMarketplaceException exception = assertThrows(
        LostArkMarketplaceException.class,
        () -> this.lostArkApiServiceImpl.getMarketData(testCurrentRequestURI));
    
    assertEquals(503, exception.getStatus());
    assertEquals(testCurrentRequestURI, exception.getPath());
  }

  @Test
  @DisplayName("getMarketData() 실패 케이스 - 잘못된 JSON 응답으로 인한 파싱 오류")
  void getMarketData_ParsingFailure() {
    String invalidJsonResponse = "invalid json";
    
    when(this.restTemplate.exchange(
        anyString(),
        eq(HttpMethod.POST),
        any(HttpEntity.class),
        eq(String.class)))
    .thenReturn(ResponseEntity.ok(invalidJsonResponse));
    
    LostArkMarketplaceException exception = assertThrows(
        LostArkMarketplaceException.class,
        () -> this.lostArkApiServiceImpl.getMarketData(testCurrentRequestURI));
    
    assertEquals(503, exception.getStatus());
  }
  
}
