package com.lostark.marketplace.service;

import com.lostark.marketplace.model.CartDto;
import com.lostark.marketplace.model.CartItemRequestDto;
import com.lostark.marketplace.model.CheckoutResponseDto;

public interface CartService {
  
  /**
   * 유저의 장바구니에 상품을 추가하는 메서드
   * 
   * @param request 상품 추가 요청을 담은 DTO
   * @param username 장바구니에 상품을 추가할 유저 이름
   * @return 장바구니 정보가 담긴 CartDto 객체
   */
  CartDto addItemToCart(CartItemRequestDto request, String username);
  
  /**
   * 장바구니에 담긴 상품의 수량을 변경하는 메서드
   * 
   * @param request 수량 변경 요청을 담은 DTO
   * @param username 장바구니에 상품 수량을 변경할 유저 이름
   * @return 특정 상품의 수량이 변경된 장바구니 정보가 담긴 CartDto 객체
   */
  CartDto updateItemQuantity(CartItemRequestDto request, String username);
  
  /**
   * 사용자의 현재 장바구니 정보를 조회하는 메서드
   * 
   * @param username 장바구니를 조회할 유저 이름
   * @return 사용자의 장바구니 정보가 담긴 CartDto 객체
   */
  CartDto getCart(String username);
  
  /**
   * 장바구니에서 특정 상품을 제거하는 메서드
   * 
   * @param itemId 제거할 상품의 ID
   * @param username 장바구니 상품을 제거할 유저 이름
   * @return 상품이 제거된 후의 장바구니 정보가 담긴 CartDto 객체
   */
  CartDto removeItemFromCart(Long itemId, String username);
  
  /**
   * 장바구니에 담긴 상품을 결제 처리하고 장바구니를 초기화하는 메서드
   * 
   * @param username 결제를 진행할 유저 이름
   * @param usedGold 결제 시 사용할 인게임 재화(골드)
   * @param usedPoints 결제 시 사용할 포인트
   * @return 결제 결과와 관련 정보를 담은 CheckoutResponseDto 객체
   */
  CheckoutResponseDto checkoutCart(String username, int usedGold, int usedPoints);
  
  /**
   * 모든 사용자의 장바구니 내 상품 가격을 주기적으로 최신 상태로 동기화하는 메서드
   * MarketData에 따른 가격 변동을 반영하여 장바구니의 상품 가격을 업데이트합니다.
   */
  void synchronizeCartPrices();
  
}
