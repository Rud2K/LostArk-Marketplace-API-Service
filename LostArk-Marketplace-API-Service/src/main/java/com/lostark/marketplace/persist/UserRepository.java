package com.lostark.marketplace.persist;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  
  /**
   * 유저 이름으로 유저 정보를 조회하는 메소드
   * 
   * @param username 유저 이름
   * @return 유저가 존재하는 경우, 해당 UserEntity 객체를 감싼 Optional
   */
  Optional<UserEntity> findByUsername(String username);
  
  /**
   * 주어진 유저 이름이 이미 존재하는지 확인하는 메소드
   * 
   * @param username 유저 이름
   * @return 유저 이름이 존재한다면 true, 존재하지 않으면 false
   */
  boolean existsByUsername(String username);
  
  /**
   * 주어진 이메일이 이미 존재하는지 확인하는 메소드
   * 
   * @param email 이메일
   * @return 이메일이 존재한다면 true, 존재하지 않으면 false
   */
  boolean existsByEmail(String email);
  
}
