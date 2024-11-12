package com.lostark.marketplace.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.PurchaseHistoryEntity;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryEntity, Long> {
  
  
  
}
