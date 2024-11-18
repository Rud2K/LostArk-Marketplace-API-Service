package com.lostark.marketplace.model.constant;

import lombok.Getter;

@Getter
public enum PointReason {
  
  PURCHASE("구매 보상"),
  DAILY_LOGIN("일일 로그인 보상"),
  COMPENSATION ("운영 보상");
  
  private final String description;
  
  PointReason(String description) {
    this.description = description;
  }
  
}
