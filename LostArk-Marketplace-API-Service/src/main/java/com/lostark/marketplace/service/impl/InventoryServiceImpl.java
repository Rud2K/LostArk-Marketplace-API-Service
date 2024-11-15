package com.lostark.marketplace.service.impl;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import com.lostark.marketplace.model.InventoryDto;
import com.lostark.marketplace.persist.InventoryRepository;
import com.lostark.marketplace.persist.UserRepository;
import com.lostark.marketplace.persist.entity.InventoryEntity;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.persist.entity.UserEntity;
import com.lostark.marketplace.service.InventoryService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
  
  private final InventoryRepository inventoryRepository;
  private final UserRepository userRepository;
  
  @Override
  public Set<InventoryDto> getUserInventory(String username) {
    // 유저 조회
    UserEntity user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // DTO로 변환 후 반환
    return user.getInventory().stream()
        .map(InventoryEntity::toDto)
        .collect(Collectors.toSet());
  }
  
  @Override
  public void addOrUpdateInventory(UserEntity user, MarketEntity item, int quantity) {
    InventoryEntity existingInventory = this.inventoryRepository.findByUserAndItem(user, item);
    
    if (existingInventory != null) {
      // 기존 인벤토리가 있을 경우 수량만 업데이트
      existingInventory.setQuantity(existingInventory.getQuantity() + quantity);
      this.inventoryRepository.save(existingInventory);
    } else {
      // 기존 인벤토리가 없을 경우 새로 생성
      InventoryEntity newInventory = InventoryEntity.builder()
          .user(user)
          .item(item)
          .quantity(quantity)
          .createdAt(LocalDateTime.now())
          .build();
      this.inventoryRepository.save(newInventory);
    }
  }
  
}
