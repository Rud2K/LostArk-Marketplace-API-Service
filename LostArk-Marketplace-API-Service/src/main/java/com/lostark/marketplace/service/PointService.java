package com.lostark.marketplace.service;

import com.lostark.marketplace.persist.entity.UserEntity;

public interface PointService {
  
  /**
   * 포인트 적립 메서드
   * 
   * @param user 포인트를 적립할 유저
   * @param totalPrice 유저가 상품을 결제한 금액
   */
  void addPoints(UserEntity user, int totalPrice);
  
  /**
   * 매일 자정 00시에 실행되어 6개월이 지난 포인트 삭제
   */
  void deleteExpiredPoints();
  
}
