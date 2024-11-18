package com.lostark.marketplace.persist;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
  
  /**
   * 특정 날짜 이전에 생성된 모든 포인트 기록을 조회하는 메서드
   * 
   * @param expirationDate 기준 날짜 (해당 날짜 이전의 포인트 기록 조회)
   * @return 지정된 날짜 이전에 생성된 포인트 기록 목록
   */
  List<PointHistoryEntity> findAllByCreatedAtBefore(LocalDateTime expirationDate);
  
  /**
   * 특정 날짜 이전에 생성된 모든 포인트 기록을 삭제하는 메서드
   * 
   * @param expirationDate 기준 날짜 (해당 날짜 이전의 포인트 기록 삭제)
   */
  @Modifying
  @Query("DELETE FROM point_history p WHERE p.createdAt < :expirationDate")
  void deleteByCreatedAtBefore(@Param("expirationDate") LocalDateTime expirationDate);
  
}
