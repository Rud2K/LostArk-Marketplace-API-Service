package com.lostark.marketplace.service;

import java.util.Set;
import com.lostark.marketplace.model.InventoryDto;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.persist.entity.UserEntity;

public interface InventoryService {
  
  /**
   * 유저 이름으로 인벤토리를 조회하는 메서드
   * 
   * @param username 유저 이름
   * @return 유저의 인벤토리 목록
   */
  Set<InventoryDto> getUserInventory(String username);
  
  /**
   * 인벤토리에 구매한 아이템을 추가 또는 수량 업데이트하는 메서드
   * 
   * @param user 구매를 진행한 유저
   * @param item 구매한 아이템
   * @param quantity 구매한 아이템 수량
   */
  void addOrUpdateInventory(UserEntity user, MarketEntity item, int quantity);
  
}
