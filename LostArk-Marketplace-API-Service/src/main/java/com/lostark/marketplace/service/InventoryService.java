package com.lostark.marketplace.service;

import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.persist.entity.UserEntity;

public interface InventoryService {
  
  /**
   * 유저의 인벤토리에 구매한 아이템을 추가 또는 수량을 업데이트하는 메서드
   * 
   * @param user 유저 정보
   * @param item 추가할 아이템 정보
   * @param quantity 구매한 아이템 수량
   */
  void addOrUpdateInventory(UserEntity user, MarketEntity item, int quantity);
  
}
