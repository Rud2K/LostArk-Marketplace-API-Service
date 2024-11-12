package com.lostark.marketplace.persist.entity;

import java.time.LocalDateTime;
import com.lostark.marketplace.model.OrderManagerDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "order_manager")
public class OrderManagerEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId; // 고유 식별자 (자동 생성)

  @ManyToOne
  @JoinColumn(name = "cart_id")
  private CartEntity cart; // 해당 주문이 포함된 장바구니 정보

  @ManyToOne
  @JoinColumn(name = "item_id")
  private MarketEntity item; // 연관된 상품 정보
  
  private Integer quantity; // 주문된 상품의 수량
  
  private Integer price; // 상품의 가격
  
  private LocalDateTime orderDate; // 해당 주문이 장바구니에 담긴 시간
  
  /**
   * OrderManagerEntity를 OrderManagerDto로 변환하는 메소드
   * 
   * @return OrderManagerDto 객체
   */
  public OrderManagerDto toDto() {
    return OrderManagerDto.builder()
        .orderId(this.orderId)
        .itemId(this.item.getItemId())
        .itemName(this.item.getItemName())
        .price(this.item.getCurrentMinPrice())
        .quantity(this.quantity)
        .orderDate(this.orderDate)
        .build();
  }
  
}
