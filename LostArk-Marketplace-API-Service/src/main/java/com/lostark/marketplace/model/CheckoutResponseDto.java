package com.lostark.marketplace.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDto {
  
  private Long userId; // 결제한 사용자 고유 식별자
  
  private Integer usedGold; // 결제 시 사용한 골드
  
  private Integer usedPoint; // 결제 시 사용한 포인트
  
  private Integer remainingGold; // 결제 후 남은 사용자 보유 골드
  
  private Integer remainingPoint; // 결제 후 남은 사용자 보유 포인트
  
  private Integer totalAmount; // 결제된 총 금액
  
  private LocalDateTime purchaseDate; // 결제 시각
  
  private List<CartItemDto> purchasedItemNames; // 구매된 상품 이름 목록
  
}
