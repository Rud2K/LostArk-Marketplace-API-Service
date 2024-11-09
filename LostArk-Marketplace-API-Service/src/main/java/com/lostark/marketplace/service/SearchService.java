package com.lostark.marketplace.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.lostark.marketplace.model.MarketResponseDto;

public interface SearchService {
  
  /**
   * 주어진 아이템 이름 리스트를 사용해 자동 완성 Trie를 초기화하는 메서드.
   * 
   * @param itemNames
   */
  void initTrie(List<String> itemNames);
  
  /**
   * 주어진 키워드를 기반으로 자동 완성 결과를 반환하는 메서드
   * 
   * @param keyword 사용자가 입력한 검색 키워드
   * @return 키워드와 일치하는 자동 완성 결과 리스트
   */
  List<String> autoComplete(String keyword);
  
  /**
   * 주어진 필터 조건과 페이지 정보를 기반으로 마켓 데이터를 검색하여 반환하는 메서드.
   * 
   * @param itemName 검색할 아이템 이름 (선택 사항)
   * @param itemGrade 검색할 아이템 등급 (선택 사항)
   * @param itemType 검색할 아이템 유형 (선택 사항)
   * @param pageable 페이지 정보 (페이지 번호, 페이지 크기 등)
   * @return 검색 조건에 맞는 마켓 데이터 리스트를 포함한 MarketResponseDto 객체
   */
  MarketResponseDto getItemsByFilter(
      String itemName,
      String itemGrade,
      String itemType,
      Pageable pageable);
  
}
