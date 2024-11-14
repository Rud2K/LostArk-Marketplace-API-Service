package com.lostark.marketplace.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lostark.marketplace.persist.entity.InventoryEntity;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.persist.entity.UserEntity;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
  
  InventoryEntity findByUserAndItem(UserEntity user, MarketEntity item);
  
}
