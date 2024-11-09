package com.lostark.marketplace.persist;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.MarketEntity;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long>, JpaSpecificationExecutor<MarketEntity> {
  
  Optional<MarketEntity> findByItemNameAndItemGrade(String itemName, String itemGrade);
  
  Page<MarketEntity> findByItemNameContaining(String keyword, Pageable pageable);
  
}
