package com.lostark.marketplace.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.lostark.marketplace.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserDto {
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUp {
    @NotEmpty(message = "Role is required")
    private String role;
    
    @NotEmpty(message = "Username is required")
    private String username;
    
    @Password
    @NotEmpty(message = "Password is required")
    private String password;
    
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;
  }
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignIn {
    @NotEmpty(message = "Username is required")
    private String username;
    
    @NotEmpty(message = "Password is required")
    private String password;
  }
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateCurrencyRequest {
    @Builder.Default
    private Optional<Integer> gold = Optional.empty();
    
    @Builder.Default
    private Optional<Integer> point = Optional.empty();
    
    @Builder.Default
    private Optional<Integer> couponCount = Optional.empty();
  }
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateEmailRequest {
    @Email(message = "Old email should be valid")
    @NotEmpty(message = "Old email is required")
    private String oldEmail;
    
    @Email(message = "New email should be valid")
    @NotEmpty(message = "New email is required")
    private String newEmail;
  }
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ChangePasswordRequest {
    @Password
    @NotEmpty(message = "Old password is required")
    private String oldPassword;
    
    @Password
    @NotEmpty(message = "New password is required")
    private String newPassword;
  }
  
  private Long userId; // 고유 식별자
  
  private String role; // 유저 권한
  
  private String username; // 유저 이름
  
  private String email; // 유저 이메일
  
  private Integer gold; // 유저가 보유 중인 인게임 재화 (골드)
  
  private Integer point; // 유저가 보유 중인 포인트
  
  private Integer couponCount; // 유저가 보유 중인 쿠폰의 개수
  
  private LocalDateTime createAt; // 계정 생성일
  
  private List<CharacterInfoDto> characterInfos; // 유저가 보유 중인 캐릭터 리스트
  
}
