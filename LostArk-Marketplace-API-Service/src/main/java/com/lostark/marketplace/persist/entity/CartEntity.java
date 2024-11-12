package com.lostark.marketplace.persist.entity;

import java.util.List;
import java.util.stream.Collectors;
import com.lostark.marketplace.model.CartDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Entity(name = "cart")
public class CartEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cartId; // 고유 식별자 (자동 생성)
  
  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user; // 해당 장바구니와 연관된 유저 정보
  
  private Integer totalPrice; // 장바구니의 총 가격
  
  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderManagerEntity> orders; // 해당 장바구니에 포함된 주문 항목 목록
  
  /**
   * CartEntity를 CartDto 변환하는 메소드
   * 
   * @return CartDto 객체
   */
  public CartDto toDto() {
    return CartDto.builder()
        .cartId(this.cartId)
        .userId(this.user.getUserId())
        .totalPrice(this.totalPrice)
        .orders(this.orders.stream()
            .map(OrderManagerEntity::toDto)
            .collect(Collectors.toList()))
        .build();
  }
  
}
