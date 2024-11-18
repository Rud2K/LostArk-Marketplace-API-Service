package com.lostark.marketplace.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.lostark.marketplace.model.constant.PointReason;
import com.lostark.marketplace.persist.PointHistoryRepository;
import com.lostark.marketplace.persist.UserRepository;
import com.lostark.marketplace.persist.entity.PointHistoryEntity;
import com.lostark.marketplace.persist.entity.UserEntity;
import com.lostark.marketplace.service.PointService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
  
  private final PointHistoryRepository pointHistoryRepository;
  private final UserRepository userRepository;
  
  private static final BigDecimal POINT_RATE = BigDecimal.valueOf(0.03); // 포인트 적립 비율
  private static final int MINIMUM_ELIGIBLE_AMOUNT = 1000; // 포인트 적립 최소 금액 기준
  private static final int DAILY_BONUS_POINTS = 10; // 일일 로그인 포인트 보상
  
  @Override
  public void awardPoints(UserEntity user, int remainingAmount) {
    // 결제 금액이 기준 이하인 경우 반환
    if (remainingAmount < MINIMUM_ELIGIBLE_AMOUNT) {
      return;
    }
    
    // 적립 포인트 계산
    int pointsToAward = BigDecimal.valueOf(remainingAmount)
        .multiply(POINT_RATE)
        .setScale(0, RoundingMode.FLOOR) // 소수점 내림 처리
        .intValue();
    
    // 적립 포인트가 0 이하인 경우 반환
    if (pointsToAward <= 0) {
      return;
    }
    
    // 유저의 포인트 업데이트
    user.setPoint(user.getPoint() + pointsToAward);
    
    // 포인트 적립 기록 생성
    PointHistoryEntity pointHistory = PointHistoryEntity.builder()
        .user(user)
        .points(pointsToAward)
        .reason(PointReason.PURCHASE.getDescription())
        .createdAt(LocalDateTime.now())
        .build();
    
    // 포인트 적립 기록 저장
    this.pointHistoryRepository.save(pointHistory);
  }
  
  @Override
  public void awardDailyLoginBonus(UserEntity user) {
    // 금일 시작과 종료 시간 계산
    LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
    LocalDateTime endOfDay = startOfDay.plusDays(1);
    
    // 금일 보상 여부 확인
    boolean hasReceivedBonus = this.pointHistoryRepository.existsByUserAndReasonAndCreatedAtBetween(
        user,
        PointReason.DAILY_LOGIN.getDescription(),
        startOfDay,
        endOfDay);
    
    if (!hasReceivedBonus) {
      // 포인트 지급
      user.setPoint(user.getPoint() + DAILY_BONUS_POINTS);
      
      // 포인트 지급 기록 생성
      PointHistoryEntity dailyBonus = PointHistoryEntity.builder()
          .user(user)
          .points(DAILY_BONUS_POINTS)
          .reason(PointReason.DAILY_LOGIN.getDescription())
          .createdAt(LocalDateTime.now())
          .build();
      
      // 포인트 지급 기록 저장
      this.pointHistoryRepository.save(dailyBonus);
    }
  }
  
  @Override
  @Scheduled(cron = "0 0 0 * * ?") // 매일 00:00에 실행
  public void deleteExpiredPoints() {
    LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
    
    // 만료된 포인트 이력 조회
    List<PointHistoryEntity> expiredPoints = pointHistoryRepository.findAllByCreatedAtBefore(sixMonthsAgo);
    
    // 유저 포인트 차감
    expiredPoints.forEach(expiredPoint -> {
      UserEntity user = expiredPoint.getUser();
      user.setPoint(Math.max(0, user.getPoint() - expiredPoint.getPoints()));
      this.userRepository.save(user);
    });
    
    // 만료된 포인트 이력 삭제
    this.pointHistoryRepository.deleteByCreatedAtBefore(sixMonthsAgo);
  }
  
}
