package com.lostark.marketplace.persist;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
  
  /**
   * 유저 이름으로 장바구니 정보를 조회하는 메서드.
   * CartEntity와 연관된 UserEntity의 username을 통해 조회.
   * 
   * @param username 유저 이름
   * @return 유저가 존재하는 경우, 해당 CartEntity 객체를 감싼 Optional
   */
  Optional<CartEntity> findByUserUsername(String username);
  
}
