package com.lostark.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDto {
  
  private Long itemId; // 장바구니에 추가할 상품의 고유 번호
  
  private Integer quantity; // 장바구니에 추가할 상품의 수량
  
}
