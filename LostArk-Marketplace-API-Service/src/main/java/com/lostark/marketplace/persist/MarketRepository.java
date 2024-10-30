package com.lostark.marketplace.persist;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.MarketEntity;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long> {
  
  Optional<MarketEntity> findByItemNameAndItemGrade(String itemName, String itemGrade);
  
}
