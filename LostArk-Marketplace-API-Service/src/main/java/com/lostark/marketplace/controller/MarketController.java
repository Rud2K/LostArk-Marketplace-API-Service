package com.lostark.marketplace.controller;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lostark.marketplace.model.MarketResponseDto;
import com.lostark.marketplace.service.SearchService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/market")
public class MarketController {
  
  private final SearchService searchService;
  
  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<List<String>> autoComplete(@RequestParam(value = "keyword") String keyword) {
    return ResponseEntity.ok(this.searchService.autoComplete(keyword));
  }
  
  @GetMapping("/search")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<MarketResponseDto> getItemsByFilter(
      @RequestParam(value = "itemName", required = false) String itemName,
      @RequestParam(value = "itemGrade", required = false) String itemGrade,
      @RequestParam(value = "itemType", required = false) String itemType,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return ResponseEntity.ok(this.searchService.getItemsByFilter(itemName, itemGrade, itemType, pageable));
  }
  
}
