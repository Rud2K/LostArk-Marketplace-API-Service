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
public class CartItemDto {
  
  private Long cartItemId; // 고유 식별자
  
  private Long itemId; // 주문된 상품의 식별자
  
  private String itemName; // 주문된 상품의 이름
  
  private Integer price; // 주문된 상품의 가격
  
  private Integer quantity; // 주문된 상품의 수량
  
  private LocalDateTime orderDate; // 해당 주문이 장바구니에 담긴 시간
  
}
