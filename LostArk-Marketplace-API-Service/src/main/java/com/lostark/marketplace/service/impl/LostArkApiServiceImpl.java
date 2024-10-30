package com.lostark.marketplace.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import com.lostark.marketplace.model.CharacterInfoDto;
import com.lostark.marketplace.model.ItemRequestDto;
import com.lostark.marketplace.model.MarketResponseDto;
import com.lostark.marketplace.model.constant.LostArkClass;
import com.lostark.marketplace.persist.MarketRepository;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.service.LostArkApiService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LostArkApiServiceImpl implements LostArkApiService {
  
  @Value("${lostark.api-key}")
  private String apiKey; // API 인증 키
  
  @Value("${lostark.base-url}")
  private String baseUrl; // API 기본 URL
  
  private final RestTemplate restTemplate;
  private final MarketRepository marketRepository;
  
  @Override
  public List<CharacterInfoDto> getCharacterData(String characterName) {
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
      return this.parseCharacterInfoList(response.getBody());
    } catch (Exception e) {
      throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE);
    }
  }
  
  /**
   * JSON 데이터를 파싱하여 List<CharacterInfo> 객체로 변환하는 메서드
   * 
   * @param jsonString API 응답 데이터
   * @return List<CharacterInfo> 객체
   * @throws LostarkMarketplaceException JSON 파싱 실패 시 발생
   */
  private List<CharacterInfoDto> parseCharacterInfoList(String jsonString) {
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
      throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE);
    }
  }
  
  @Override
  @Scheduled(fixedRate = 300000) // 5분마다 실행 (300000밀리초)
  public void updateMarketData() {
    // 마켓 정보를 가져와서 저장
    List<MarketResponseDto> marketDataList = this.getMarketData();
    this.saveMarketDataToDatabase(marketDataList);
  }
  
  @Override
  public List<MarketResponseDto> getMarketData() {
    // 요청 객체 생성
    ItemRequestDto tierFourMaterialRequest = new ItemRequestDto("GRADE", 50000, "", 3, "", "", 1, "ASC");
    ItemRequestDto tierFiveMaterialRequest = new ItemRequestDto("GRADE", 50000, "", 4, "", "", 1, "ASC");
    ItemRequestDto legendaryEnravingRecipeRequest = new ItemRequestDto("GRADE", 40000, "", null, "전설", "", 1, "ASC");
    ItemRequestDto relicEnravingRecipeRequest = new ItemRequestDto("GRADE", 40000, "", null, "유물", "", 1, "ASC");
    
    // 각 요청에 대한 결과를 저장할 List 생성
    List<MarketResponseDto> marketResponses = new ArrayList<>();
    
    // 각각의 요청을 반복적으로 호출
    for (ItemRequestDto request : List.of(
        tierFourMaterialRequest,
        tierFiveMaterialRequest,
        legendaryEnravingRecipeRequest,
        relicEnravingRecipeRequest)) {
      
      // 페이지 수를 확인을 위한 초기 변수 설정
      int pageNo = 1;
      int totalPages;
      
      do {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + this.apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // 응답 받은 페이지 번호를 설정하여 요청 데이터 업데이트
        request.setPageNo(pageNo);
        HttpEntity<ItemRequestDto> entity = new HttpEntity<>(request, headers);
        
        // API 호출 및 응답 받기
        try {
          ResponseEntity<String> response = this.restTemplate.exchange(
              this.baseUrl + "/markets/items",
              HttpMethod.POST,
              entity,
              String.class);
          
          // JSON 응답 데이터를 파싱하여 MarketResponseDto로 변환
          MarketResponseDto marketData = this.parseMarketData(response.getBody());
          marketResponses.add(marketData);
          
          // 전체 페이지 수 설정
          totalPages = marketData.getPageSize();
          pageNo++;
        } catch (Exception e) {
          throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE);
        }
      } while (pageNo <= totalPages);
    }
    
    return marketResponses;
  }
  
  /**
   * JSON 데이터를 파싱하여 MarketResponseDto 객체로 변환하는 메서드.
   * 
   * @param jsonString API 응답 데이터
   * @return MarketResponseDto 거래소 아이템 데이터 리스트
   * @throws LostarkMarketplaceException JSON 파싱 실패 시 발생
   */
  private MarketResponseDto parseMarketData(String jsonString) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(jsonString, MarketResponseDto.class);
    } catch (Exception e) {
      throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE);
    }
  }
  
  /**
   * 마켓 데이터를 DB에 저장하는 메서드
   * 
   * @param marketDataList 가져온 마켓 데이터 목록
   */
  private void saveMarketDataToDatabase(List<MarketResponseDto> marketDataList) {
    List<MarketEntity> entitiesToSave = new ArrayList<>();
    List<MarketEntity> entitiesToUpdate = new ArrayList<>();
    
    marketDataList.forEach(data -> {
      data.getItems().forEach(item -> {
        Optional<MarketEntity> existingEntity =
            this.marketRepository.findByItemNameAndItemGrade(item.getName(), item.getGrade());
        
        if (existingEntity.isPresent()) {
          // 기존 데이터가 존재하는 경우, 변경된 항목이 있을 때만 업데이트 리스트에 추가
          MarketEntity entityToUpdate = existingEntity.get();
          
          if (!entityToUpdate.getYDayAvgPrice().equals(item.getYDayAvgPrice())
              || !entityToUpdate.getRecentPrice().equals(item.getRecentPrice())
              || !entityToUpdate.getCurrentMinPrice().equals(item.getCurrentMinPrice())) {
            
            entityToUpdate.setYDayAvgPrice(item.getYDayAvgPrice());
            entityToUpdate.setRecentPrice(item.getRecentPrice());
            entityToUpdate.setCurrentMinPrice(item.getCurrentMinPrice());
            
            entitiesToUpdate.add(entityToUpdate);
          }
        } else {
          // 새로운 데이터인 경우, 삽입 리스트에 추가
          entitiesToSave.add(MarketEntity.builder()
              .itemName(item.getName())
              .itemGrade(item.getGrade())
              .icon(item.getIcon())
              .bundleCount(item.getBundleCount())
              .tradeRemainCount(item.getTradeRemainCount())
              .yDayAvgPrice(item.getYDayAvgPrice())
              .recentPrice(item.getRecentPrice())
              .currentMinPrice(item.getCurrentMinPrice())
              .build());
        }
      });
    });
    
    // Batch Insert
    if (!entitiesToSave.isEmpty()) {
      marketRepository.saveAll(entitiesToSave);
    }
    
    // Batch Update
    if (!entitiesToUpdate.isEmpty()) {
      marketRepository.saveAll(entitiesToUpdate);
    }
  }
  
}