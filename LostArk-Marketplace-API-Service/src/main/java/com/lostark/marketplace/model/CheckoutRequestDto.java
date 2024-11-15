package com.lostark.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDto {
  
  private int usedGold; // 사용할 인게임 재화(골드)
  private int usedPoint; // 사용할 포인트
  
}
