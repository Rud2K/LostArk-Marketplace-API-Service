package com.lostark.marketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.lostark.marketplace.model.UserDto;
import com.lostark.marketplace.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
  
  private final UserService userService;
  
  @PostMapping("/signup")
  public ResponseEntity<UserDto> signUp(@Valid @RequestBody UserDto.SignUp request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.signUp(request));
  }
  
  @PostMapping("signin")
  public ResponseEntity<String> signIn(@Valid @RequestBody UserDto.SignIn request) {
    return ResponseEntity.ok(this.userService.signIn(request));
  }
  
  @GetMapping("/profile")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<UserDto> getProfile() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.userService.getProfile(username));
  }
  
  @PatchMapping("/{userId}/currency")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDto> updateUserCurrencyByAdmin(
      @PathVariable("userId") Long userId,
      @Valid @RequestBody UserDto.UpdateCurrencyRequest request) {
    return ResponseEntity.ok(this.userService.updateUserCurrencyByAdmin(userId, request));
  }
  
  @PatchMapping("/email")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserDto> updateEmail(@Valid @RequestBody UserDto.UpdateEmailRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.userService.updateEmail(username, request));
  }
  
  @PutMapping("/password")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> changePassword(@Valid @RequestBody UserDto.ChangePasswordRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    this.userService.changePassword(username, request);
    return ResponseEntity.ok("비밀번호 변경이 성공적으로 완료되었습니다.");
  }
  
  @PostMapping("/password")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<String> resetPassword() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.userService.resetPassword(username));
  }
  
  @PostMapping("/lostark/character")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<UserDto> syncLostArkCharacters(@RequestParam("characterName") String characterName) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(this.userService.syncLostArkCharacters(username, characterName));
  }
  
}
