package com.lostark.marketplace.service;

import java.util.Set;
import com.lostark.marketplace.model.InventoryDto;

public interface InventoryService {
  
  /**
   * 유저 이름으로 인벤토리를 조회하는 메서드
   * 
   * @param username 유저 이름
   * @return 유저의 인벤토리 목록
   */
  Set<InventoryDto> getUserInventory(String username);
  
}
