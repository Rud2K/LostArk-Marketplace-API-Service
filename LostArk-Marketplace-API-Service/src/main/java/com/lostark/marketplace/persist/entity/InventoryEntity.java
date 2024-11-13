package com.lostark.marketplace.persist.entity;

import java.time.LocalDateTime;
import com.lostark.marketplace.model.InventoryDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Entity
@Table(name = "inventory", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "item_id"})
})
public class InventoryEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long inventoryId; // 고유 식별자
  
  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user; // 인벤토리를 소유 중인 유저 정보
  
  @ManyToOne
  @JoinColumn(name = "item_id", nullable = false)
  private MarketEntity item; // 유저가 보유하고 있는 아이템 정보
  
  private Integer quantity; // 유저가 보유하고 있는 아이템의 수량
  
  private LocalDateTime createdAt; // 인벤토리 생성일
  
  /**
   * InventoryEntity를 InventoryDto로 변환하는 메소드
   * 
   * @return InventoryDto 객체
   */
  public InventoryDto toDto() {
    return InventoryDto.builder()
        .itemId(this.getItem().getItemId())
        .itemName(this.getItem().getItemName())
        .quantity(this.quantity)
        .createdAt(this.createdAt)
        .build();
  }
  
}
