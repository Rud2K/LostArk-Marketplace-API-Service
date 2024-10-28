package com.lostark.marketplace.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.CharacterInfoEntity;

@Repository
public interface CharacterInfoRepository extends JpaRepository<CharacterInfoEntity, Long> {
  
  
  
}
