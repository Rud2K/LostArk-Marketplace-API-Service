package com.lostark.marketplace.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
  
  private Long cartId; // 고유 식별자
  
  private Long userId; // 장바구니를 소유 중인 유저
  
  private Integer totalPrice; // 장바구니의 총 가격
  
  private List<CartItemDto> orders; // 해당 장바구니에 포함된 주문 항목 목록
  
}
