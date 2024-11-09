package com.lostark.marketplace.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.lostark.marketplace.model.ItemResponseDto;
import com.lostark.marketplace.model.MarketResponseDto;
import com.lostark.marketplace.persist.MarketRepository;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.service.SearchService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
  
  private final MarketRepository marketRepository;
  private final Trie<String, String> trie;
  
  @Override
  public void initTrie(List<String> itemNames) {
    this.trie.clear();
    itemNames.forEach(name -> this.trie.put(name, name));
  }
  
  @Override
  public List<String> autoComplete(String keyword) {
    return this.trie.prefixMap(keyword).keySet().stream()
        .limit(5)
        .collect(Collectors.toList());
  }
  
  @Override
  public MarketResponseDto getItemsByFilter(
      String itemName,
      String itemGrade,
      String itemType,
      Pageable pageable) {
    // Specification을 사용해 동적 쿼리 사용
    Specification<MarketEntity> specification = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      
      // 이름 조건 추가
      if (itemName != null && !itemName.isEmpty()) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("itemName")), "%" + itemName.toLowerCase() + "%"));
      }
      
      // 등급 조건 추가
      if (itemGrade != null && !itemGrade.isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("itemGrade"), itemGrade));
      }
      
      // 유형 조건 추가
      if (itemType != null && !itemType.isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("itemType"), itemType));
      }
      
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
    
    Page<MarketEntity> searchResult = this.marketRepository.findAll(specification, pageable);
    
    return MarketResponseDto.builder()
        .pageNo(searchResult.getNumber() + 1)
        .pageSize(searchResult.getSize())
        .totalCount((int) searchResult.getTotalElements())
        .items(searchResult.getContent().stream().map(item -> ItemResponseDto.builder()
            .id(item.getItemId())
            .name(item.getItemName())
            .grade(item.getItemGrade())
            .icon(item.getIcon())
            .bundleCount(item.getBundleCount())
            .tradeRemainCount(item.getTradeRemainCount())
            .yDayAvgPrice(item.getYDayAvgPrice())
            .recentPrice(item.getRecentPrice())
            .currentMinPrice(item.getCurrentMinPrice())
            .itemType(item.getItemType())
            .build())
            .collect(Collectors.toList()))
        .build();
  }
  
}
