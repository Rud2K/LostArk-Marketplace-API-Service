package com.lostark.marketplace.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketResponseDto {
  
  @JsonProperty("PageNo")
  private Integer pageNo; // 현재 페이지 번호
  
  @JsonProperty("PageSize")
  private Integer pageSize; // 페이지당 아이템 수
  
  @JsonProperty("TotalCount")
  private Integer totalCount; // 전체 아이템 수
  
  @JsonProperty("Items")
  private List<ItemResponseDto> items; // 아이템 목록
  
}
