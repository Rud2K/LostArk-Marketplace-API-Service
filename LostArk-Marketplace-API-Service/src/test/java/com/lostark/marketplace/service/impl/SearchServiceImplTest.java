package com.lostark.marketplace.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.List;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.lostark.marketplace.model.MarketResponseDto;
import com.lostark.marketplace.model.constant.ItemType;
import com.lostark.marketplace.persist.MarketRepository;
import com.lostark.marketplace.persist.entity.MarketEntity;

class SearchServiceImplTest {
  
  @Mock
  private MarketRepository marketRepository;
  
  @InjectMocks
  private SearchServiceImpl searchServiceImpl;
  
  private List<String> itemNames;
  private String keyword;
  private String itemName;
  private String itemGrade;
  private String itemType;
  private MarketEntity mockEntity;
  private Pageable pageable;
  private Page<MarketEntity> mockPage;
  
  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    
    Trie<String, String> trie = new PatriciaTrie<>();
    this.searchServiceImpl = new SearchServiceImpl(this.marketRepository, trie);
    
    this.itemNames = List.of("testItem1", "testItem2", "testItem3");
    
    this.keyword = "test";
    this.itemName = "testItem";
    this.itemGrade = "testGrade";
    this.itemType = ItemType.ETC.getDisplayName();
    this.mockEntity = MarketEntity.builder()
        .itemId(1L)
        .itemName("testItem")
        .itemGrade("testGrade")
        .icon("testIconURI")
        .bundleCount(1)
        .tradeRemainCount(null)
        .yDayAvgPrice(100.0)
        .recentPrice(100)
        .currentMinPrice(100)
        .itemType(ItemType.ETC.getDisplayName())
        .build();
    this.pageable = PageRequest.of(0, 10);
    this.mockPage = new PageImpl<>(List.of(this.mockEntity));
  }
  
  @Test
  @DisplayName("initTrie() 성공 케이스 - Trie 초기화 성공")
  void initTrie_Success() {
    this.searchServiceImpl.initTrie(this.itemNames);
    
    assertEquals(3, searchServiceImpl.autoComplete("test").size());
  }
  
  @Test
  @DisplayName("autoComplete() 성공 케이스 - 상품 검색 시 검색어 자동 완성 성공")
  void autoComplete_Success() throws IOException {
    this.searchServiceImpl.initTrie(this.itemNames);
    
    List<String> result = this.searchServiceImpl.autoComplete(this.keyword);
    
    assertNotNull(result);
    assertTrue(result.contains("testItem1"));
    assertTrue(result.contains("testItem2"));
    assertEquals(3, result.size());
  }
  
  @Test
  @DisplayName("autoComplete() 실패 케이스 - 검색어에 대한 결과 없음")
  void autoComplete_NoResult() {
    this.searchServiceImpl.initTrie(List.of("sampleItem1", "sampleItem2"));
    
    List<String> result = this.searchServiceImpl.autoComplete(this.keyword);
    
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("getItemsByFilter() 성공 케이스 - 상품 검색 시 필터링 검색 성공")
  void getItemsByFilter_Success() {
    when(this.marketRepository.findAll(any(Specification.class), eq(this.pageable))).thenReturn(this.mockPage);
    
    MarketResponseDto response = this.searchServiceImpl.getItemsByFilter(
        this.itemName,
        this.itemGrade,
        this.itemType,
        this.pageable);
    
    assertNotNull(response);
    assertEquals(1, response.getItems().size());
    assertEquals("testItem", response.getItems().get(0).getName());
    assertEquals("testGrade", response.getItems().get(0).getGrade());
    assertEquals(ItemType.ETC.getDisplayName(), response.getItems().get(0).getItemType());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("getItemsByFilter() 실패 케이스 - 입력 조건에 맞는 데이터가 미존재")
  void getItemsByFilter_Failure_EmptyResult() {
    when(this.marketRepository.findAll(any(Specification.class), eq(this.pageable))).thenReturn(new PageImpl<>(List.of()));
    
    MarketResponseDto response = this.searchServiceImpl.getItemsByFilter(
        this.itemName,
        this.itemGrade,
        this.itemType,
        this.pageable);
    
    assertNotNull(response);
    assertTrue(response.getItems().isEmpty());
    assertEquals(0, response.getTotalCount());
  }
  
}
