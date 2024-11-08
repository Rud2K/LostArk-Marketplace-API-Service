package com.lostark.marketplace.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.lostark.marketplace.model.MarketResponseDto;

public interface SearchService {
  
  void initTrie(List<String> itemNames);
  
  List<String> autoComplete(String keyword);
  
  MarketResponseDto getItemsByFilter(
      String itemName,
      String itemGrade,
      String itemType,
      Pageable pageable);
  
}
