package com.lostark.marketplace.persist;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lostark.marketplace.persist.entity.PointHistoryEntity;
import com.lostark.marketplace.persist.entity.UserEntity;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
  
  /**
   * 특정 유저가 지정된 이유와 날짜 범위 내에 포인트 기록이 존재하는지 확인하는 메서드
   * 
   * @param user   확인할 유저
   * @param reason 포인트 적립 사유
   * @param start  날짜 범위 시작 (포함)
   * @param end    날짜 범위 끝 (포함)
   * @return 포인트 기록이 존재하면 true, 없으면 false
   */
  boolean existsByUserAndReasonAndCreatedAtBetween(UserEntity user, String reason, LocalDateTime start, LocalDateTime end);
  
}
