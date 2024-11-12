package com.lostark.marketplace.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lostark.marketplace.persist.entity.InventoryEntity;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.persist.entity.UserEntity;
import com.lostark.marketplace.service.InventoryService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
  
  @Override
  public void addOrUpdateInventory(UserEntity user, MarketEntity item, int quantity) {
    // 유저의 인벤토리에서 구매한 아이템을 찾기
    Optional<InventoryEntity> existingInventory = user.getInventory().stream()
        .filter(inventory -> inventory.getItem().getItemId().equals(item.getItemId()))
        .findFirst();
    
    if (existingInventory.isPresent()) { // 구매한 아이템이 인벤토리에 존재하는 경우
      // 수량 업데이트
      InventoryEntity inventory = existingInventory.get();
      inventory.setQuantity(inventory.getQuantity() + quantity);
    } else { // 구매한 아이템이 인벤토리에 존재하지 않는 경우
      // 새 인벤토리 항목 생성 및 추가
      InventoryEntity newInventoryItem = InventoryEntity.builder()
          .user(user)
          .item(item)
          .quantity(quantity)
          .createdAt(LocalDateTime.now())
          .build();
      user.getInventory().add(newInventoryItem);
    }
  }
  
}
