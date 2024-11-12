package com.lostark.marketplace.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.OrderManagerEntity;

@Repository
public interface OrderManagerRepository extends JpaRepository<OrderManagerEntity, Long> {
  
  
  
}
