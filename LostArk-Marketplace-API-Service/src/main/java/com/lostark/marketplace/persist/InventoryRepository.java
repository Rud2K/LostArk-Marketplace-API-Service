package com.lostark.marketplace.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lostark.marketplace.persist.entity.InventoryEntity;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
  
  
  
}
