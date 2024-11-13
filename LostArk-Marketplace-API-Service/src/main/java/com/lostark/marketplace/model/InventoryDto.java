package com.lostark.marketplace.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryDto {
  
  private Long itemId; // 아이템 고유 식별자
  
  private String itemName; // 아이템 이름
  
  private Integer quantity; // 보유한 아이템 수량
  
  private LocalDateTime createdAt; // 인벤토리에 추가된 시간
  
}
