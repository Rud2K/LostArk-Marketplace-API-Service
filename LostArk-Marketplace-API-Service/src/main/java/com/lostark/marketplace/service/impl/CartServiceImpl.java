package com.lostark.marketplace.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import com.lostark.marketplace.model.CartDto;
import com.lostark.marketplace.model.CartItemRequestDto;
import com.lostark.marketplace.model.CheckoutResponseDto;
import com.lostark.marketplace.model.OrderManagerDto;
import com.lostark.marketplace.persist.CartRepository;
import com.lostark.marketplace.persist.MarketRepository;
import com.lostark.marketplace.persist.PurchaseHistoryRepository;
import com.lostark.marketplace.persist.entity.CartEntity;
import com.lostark.marketplace.persist.entity.MarketEntity;
import com.lostark.marketplace.persist.entity.OrderManagerEntity;
import com.lostark.marketplace.persist.entity.PurchaseHistoryEntity;
import com.lostark.marketplace.persist.entity.UserEntity;
import com.lostark.marketplace.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
  
  private final CartRepository cartRepository;
  private final MarketRepository marketRepository;
  private final PurchaseHistoryRepository purchaseHistoryRepository;
  
  @Override
  public CartDto addItemToCart(CartItemRequestDto request, String username) {
    // 유저의 장바구니 조회
    CartEntity cart = this.cartRepository.findByUserUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 장바구니에 동일한 상품이 있는지 확인
    OrderManagerEntity existingOrder = cart.getOrders().stream()
        .filter(order -> order.getItem().getItemId().equals(request.getItemId()))
        .findFirst()
        .orElse(null);
    
    if (existingOrder != null) { // 장바구니에 동일한 상품이 있는 경우
      // 기존 상품의 상태 변경
      existingOrder.setPrice(existingOrder.getItem().getCurrentMinPrice());
      existingOrder.setQuantity(existingOrder.getQuantity() + request.getQuantity());
      existingOrder.setOrderDate(LocalDateTime.now());
    } else { // 장바구니에 동일한 상품이 존재하지 않는 경우
      // 새로운 주문 항목 생성
      MarketEntity item = this.marketRepository.findById(request.getItemId())
          .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
      
      OrderManagerEntity newOrder = OrderManagerEntity.builder()
          .cart(cart)
          .item(item)
          .quantity(request.getQuantity())
          .price(item.getCurrentMinPrice())
          .orderDate(LocalDateTime.now())
          .build();
      
      // 양방향 연관 관계 설정
      newOrder.setCart(cart);
      cart.getOrders().add(newOrder);
    }
    
    // 총 가격 업데이트
    this.updateTotalPrice(cart);
    
    // 영속성 컨텍스트에 반영
    cart = this.cartRepository.save(cart);
    
    return cart.toDto();
  }
  
  @Override
  public CartDto updateItemQuantity(CartItemRequestDto request, String username) {
    // 유저의 장바구니 조회
    CartEntity cart = this.cartRepository.findByUserUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 장바구니 내 항목 조회
    OrderManagerEntity orderToUpdate = cart.getOrders().stream()
        .filter(order -> order.getItem().getItemId().equals(request.getItemId()))
        .findFirst()
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 수량 업데이트
    if (request.getQuantity() > 0) {
      orderToUpdate.setQuantity(request.getQuantity());
    } else {
      // 수량이 0이라면 해당 항목 제거
      cart.getOrders().remove(orderToUpdate);
    }
    
    // 총 가격 업데이트
    this.updateTotalPrice(cart);
    
    return cart.toDto();
  }
  
  @Override
  public CartDto getCart(String username) {
    // 유저의 장바구니 조회
    CartEntity cart = this.cartRepository.findByUserUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    return cart.toDto();
  }
  
  @Override
  public CartDto removeItemFromCart(Long itemId, String username) {
    // 유저의 장바구니 조회
    CartEntity cart = this.cartRepository.findByUserUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 장바구니 내 항목 조회
    OrderManagerEntity orderToRemove = cart.getOrders().stream()
        .filter(order -> order.getItem().getItemId().equals(itemId))
        .findFirst()
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 항목 제거
    cart.getOrders().remove(orderToRemove);
    
    // 총 가격 업데이트
    this.updateTotalPrice(cart);
    
    return cart.toDto();
  }
  
  @Override
  public CheckoutResponseDto checkoutCart(String username) {
    // 유저의 장바구니 조회
    CartEntity cart = this.cartRepository.findByUserUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    if (cart.getOrders().isEmpty()) {
      throw new LostArkMarketplaceException(HttpStatusCode.BAD_REQUEST);
    }
    
    // 결제 가능 여부 확인
    UserEntity user = cart.getUser();
    
    if (user.getGold() < cart.getTotalPrice()) {
      throw new LostArkMarketplaceException(HttpStatusCode.PAYMENT_REQUIRED);
    }
    
    // 결제 처리
    user.setGold(user.getGold() - cart.getTotalPrice());
    
    // 구매 내역 생성 및 저장
    PurchaseHistoryEntity purchaseHistory = PurchaseHistoryEntity.builder()
        .user(user)
        .totalAmount(cart.getTotalPrice())
        .purchaseDate(LocalDateTime.now())
        .purchasedItems(new ArrayList<>(cart.getOrders()))
        .build();
    
    this.purchaseHistoryRepository.save(purchaseHistory);
    
    // 구매된 항목을 OrderManagerDto로 변환
    List<OrderManagerDto> purchasedItemsDto = cart.getOrders().stream()
        .map(order -> OrderManagerDto.builder()
            .orderId(order.getOrderId())
            .itemId(order.getItem().getItemId())
            .itemName(order.getItem().getItemName())
            .price(order.getItem().getCurrentMinPrice())
            .quantity(order.getQuantity())
            .orderDate(order.getOrderDate())
            .build())
        .collect(Collectors.toList());
    
    // 응답 객체 생성
    CheckoutResponseDto response = CheckoutResponseDto.builder()
        .userId(user.getUserId())
        .remainingGold(user.getGold())
        .remainingPoint(user.getPoint())
        .totalAmount(purchaseHistory.getTotalAmount())
        .purchaseDate(purchaseHistory.getPurchaseDate())
        .purchasedItemNames(purchasedItemsDto)
        .build();
    
    // 장바구니 초기화
    cart.getOrders().forEach(order -> order.setCart(null));
    cart.getOrders().clear();
    cart.setTotalPrice(0);
    
    // 장바구니 초기화 정보를 저장해 영속성 컨텍스트에 반영
    this.cartRepository.save(cart);
    
    return response;
  }
  
  @Override
  public void synchronizeCartPrices() {
    // 모든 장바구니 조회
    List<CartEntity> carts = this.cartRepository.findAll();
    
    for (CartEntity cart : carts) {
      boolean priceUpdated = false;
      
      for (OrderManagerEntity order : cart.getOrders()) {
        // MarketData에서 최신 가격 조회
        Integer currentPrice = this.marketRepository.findById(order.getItem().getItemId())
            .map(marketItem -> marketItem.getCurrentMinPrice())
            .orElse(order.getPrice()); // 데이터가 없으면 최신 가격으로 유지
        
        if (!currentPrice.equals(order.getPrice())) {
          // 최신 가격으로 업데이트
          order.setPrice(currentPrice);
          priceUpdated = true;
        }
      }
      
      // 장바구니 총 가격 업데이트 및 업데이트된 장바구니 저장
      if (priceUpdated) {
        this.updateTotalPrice(cart);
        this.cartRepository.save(cart);
      }
    }
  }
  
  /**
   * 장바구니의 총 가격을 업데이트하는 메서드
   * 
   * @param cart 업데이트할 장바구니 엔티티
   */
  private void updateTotalPrice(CartEntity cart) {
    int totalPrice = 0;
    for (OrderManagerEntity order : cart.getOrders()) {
      totalPrice += order.getItem().getCurrentMinPrice() * (order.getQuantity() * order.getItem().getBundleCount());
    }
    cart.setTotalPrice(totalPrice);
  }
  
}