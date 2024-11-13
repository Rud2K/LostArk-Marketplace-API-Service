package com.lostark.marketplace.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import com.lostark.marketplace.model.InventoryDto;
import com.lostark.marketplace.persist.UserRepository;
import com.lostark.marketplace.persist.entity.InventoryEntity;
import com.lostark.marketplace.persist.entity.UserEntity;
import com.lostark.marketplace.service.InventoryService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
  
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
  
}
