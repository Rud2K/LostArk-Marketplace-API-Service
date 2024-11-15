package com.lostark.marketplace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lostark.marketplace.model.CartDto;
import com.lostark.marketplace.model.CartItemRequestDto;
import com.lostark.marketplace.model.CheckoutRequestDto;
import com.lostark.marketplace.model.CheckoutResponseDto;
import com.lostark.marketplace.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class CartController {
  
  private final CartService cartService;
  
  @PostMapping("/items")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<CartDto> addItemToCart(@RequestBody CartItemRequestDto request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.cartService.addItemToCart(request, username));
  }
  
  @PutMapping("/items")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<CartDto> updateItemQuantity(@RequestBody CartItemRequestDto request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.cartService.updateItemQuantity(request, username));
  }
  
  @GetMapping
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<CartDto> getCart() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.cartService.getCart(username));
  }
  
  @DeleteMapping("/items/{itemId}")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<CartDto> removeItemFromCart(@PathVariable("itemId") Long itemId) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.cartService.removeItemFromCart(itemId, username));
  }
  
  @PostMapping("/checkout")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<CheckoutResponseDto> checkoutCart(@Valid @RequestBody CheckoutRequestDto request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.cartService.checkoutCart(username, request.getUsedGold(), request.getUsedPoint()));
  }
  
}
