package com.lostark.marketplace.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.List;
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
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;

class SearchServiceImplTest {
  
  @Mock
  private ElasticsearchClient elasticsearchClient;
  
  @Mock
  private MarketRepository marketRepository;
  
  @InjectMocks
  private SearchServiceImpl searchServiceImpl;
  
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
  
  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("autoComplete() 성공 케이스 - 상품 검색 시 검색어 자동 완성 성공")
  void autoComplete_Success() throws IOException {
    Hit<MarketEntity> hit = mock(Hit.class);
    when(hit.source()).thenReturn(this.mockEntity);
    
    HitsMetadata<MarketEntity> hitsMetadata = mock(HitsMetadata.class);
    when(hitsMetadata.hits()).thenReturn(List.of(hit));
    
    SearchResponse<MarketEntity> mockResponse = mock(SearchResponse.class);
    when(mockResponse.hits()).thenReturn(hitsMetadata);
    
    when(this.elasticsearchClient.search(any(SearchRequest.class), eq(MarketEntity.class))).thenReturn(mockResponse);
    
    List<String> result = this.searchServiceImpl.autoComplete(this.keyword);
    
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("testItem", result.get(0));
  }
  
  @Test
  @DisplayName("autoComplete() 실패 케이스 - ElasticsearchClient.search() 호출 시 입출력 예외 발생")
  void autoComplete_Failure_ExceptionOccurred() throws IOException {
    when(this.elasticsearchClient.search(any(SearchRequest.class), eq(MarketEntity.class))).thenThrow(new IOException());
    
    assertThrows(RuntimeException.class, () -> this.searchServiceImpl.autoComplete(keyword));
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
