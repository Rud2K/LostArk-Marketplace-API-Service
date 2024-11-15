package com.lostark.marketplace.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
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
  
  @Override
  public void addPoints(UserEntity user, int totalPrice) {
    // 구매 금액의 일정 비율(3%)을 포인트로 환산
    int pointAmount = totalPrice * 3 / 100;
    
    if (pointAmount <= 0) {
      throw new LostArkMarketplaceException(HttpStatusCode.BAD_REQUEST);
    }
    
    // 포인트 적립
    user.setPoint(user.getPoint() + pointAmount);
    
    // 포인트 적립 기록 생성
    PointHistoryEntity pointHistory = PointHistoryEntity.builder()
        .user(user)
        .points(pointAmount)
        .reason(PointReason.PURCHASE.getDescription())
        .createdAt(LocalDateTime.now())
        .build();
    
    // 포인트 적립 기록 저장
    this.pointHistoryRepository.save(pointHistory);
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
