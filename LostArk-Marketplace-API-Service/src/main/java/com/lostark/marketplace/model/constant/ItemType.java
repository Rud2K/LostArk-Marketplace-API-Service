package com.lostark.marketplace.model.constant;

import lombok.Getter;

@Getter
public enum ItemType {
  
  MATERIAL("강화 재료"),
  ENRAVING_RECIPE("각인서"),
  ETC("기타");
  
  private final String displayName;
  
  ItemType(String displayName) {
    this.displayName = displayName;
  }
  
  public static ItemType fromValue(String value) {
    for (ItemType type : ItemType.values()) {
      if (type.displayName.equals(value)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown itemType: " + value);
  }
  
}
