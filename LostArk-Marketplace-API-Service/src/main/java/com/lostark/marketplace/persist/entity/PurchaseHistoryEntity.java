package com.lostark.marketplace.persist.entity;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Entity(name = "purchase_history")
public class PurchaseHistoryEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long purchaseHistoryId; // 고유 식별자 (자동 생성)
  
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user; // 결제한 유저 정보
  
  private Integer totalAmount; // 결제된 총 금액
  
  private LocalDateTime purchaseDate; // 결제 시각
  
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "purchase_history_id")
  private List<OrderManagerEntity> purchasedItems; // 구매한 항목 목록
  
}
