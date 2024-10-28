package com.lostark.marketplace.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import com.lostark.marketplace.model.CharacterInfoDto;
import com.lostark.marketplace.model.constant.LostArkClass;
import com.lostark.marketplace.service.LostArkApiService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostArkApiServiceImpl implements LostArkApiService {
  
  @Value("${lostark.api-key}")
  private String apiKey; // API 인증 키
  
  @Value("${lostark.base-url}")
  private String baseUrl; // API 기본 URL
  
  private final RestTemplate restTemplate;
  
  @Override
  public List<CharacterInfoDto> getCharacterData(String characterName, String currentRequestURI) {
    // HTTP 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + this.apiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    // 헤더를 포함한 엔티티 생성
    HttpEntity<String> entity = new HttpEntity<>(headers);
    
    // API 호출 및 응답 받기
    try {
      ResponseEntity<String> response = this.restTemplate.exchange(
          this.baseUrl + "/characters/" + characterName + "/siblings",
          HttpMethod.GET,
          entity,
          String.class);
      return this.parseCharacterInfoList(response.getBody(), currentRequestURI);
    } catch (Exception e) {
      throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE, currentRequestURI);
    }
  }
  
  /**
   * JSON 데이터를 파싱하여 List<CharacterInfo> 객체로 변환하는 메서드
   * 
   * @param jsonString API 응답 데이터
   * @param currentRequestURI 현재 요청의 URI
   * @return List<CharacterInfo> 객체
   * @throws LostarkMarketplaceException JSON 파싱 실패 시 발생
   */
  private List<CharacterInfoDto> parseCharacterInfoList(String jsonString, String currentRequestURI) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      List<CharacterInfoDto> characterInfoDtos = objectMapper.readValue(
          jsonString,
          new TypeReference<List<CharacterInfoDto>>() {});
      
      // 한글로 받아온 데이터를 영어 Enum으로 변환
      characterInfoDtos.forEach(dto -> dto
          .setCharacterClassName(LostArkClass.fromKoreanName(dto.getCharacterClassName()).name()));
      
      return characterInfoDtos;
    } catch (Exception e) {
      throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE, currentRequestURI);
    }
  }
  
}
