package com.lostark.marketplace.service;

import com.lostark.marketplace.persist.entity.UserEntity;

public interface PointService {
  
  /**
   * 포인트 적립 메서드
   * 
   * @param user 포인트를 적립할 유저
   * @param remainingAmount 유저가 실제 결제한 금액 (포인트 사용 금액 제외)
   */
  void awardPoints(UserEntity user, int remainingAmount);
  
  /**
   * 금일 일일 로그인 포인트 보상을 지급하는 메서드
   * 
   * @param user 보상을 받을 유저
   */
  void awardDailyLoginBonus(UserEntity user);
  
  /**
   * 매일 자정 00시에 실행되어 6개월이 지난 포인트 삭제
   */
  void deleteExpiredPoints();
  
}
