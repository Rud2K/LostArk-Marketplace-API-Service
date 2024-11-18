package com.lostark.marketplace.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutRequestDto {
  
  @PositiveOrZero
  @Builder.Default
  private int usedGold = 0; // 사용할 인게임 재화(골드)
  
  @PositiveOrZero
  @Builder.Default
  private int usedPoint = 0; // 사용할 포인트
  
}
