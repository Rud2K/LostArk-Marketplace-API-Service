package com.lostark.marketplace.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.lostark.marketplace.persist.entity.InventoryEntity;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.persist.entity.UserEntity;
import jakarta.persistence.LockModeType;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
  
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT i FROM InventoryEntity i WHERE i.user = :user AND i.item = :item")
  InventoryEntity findByUserAndItemWithLock(@Param("user") UserEntity user, @Param("item") MarketEntity item);
  
}
