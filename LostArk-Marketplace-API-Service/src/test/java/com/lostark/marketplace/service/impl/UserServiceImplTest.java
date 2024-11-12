package com.lostark.marketplace.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.model.UserDto;
import com.lostark.marketplace.model.constant.UserRole;
import com.lostark.marketplace.persist.CharacterInfoRepository;
import com.lostark.marketplace.persist.UserRepository;
import com.lostark.marketplace.persist.entity.UserEntity;
import com.lostark.marketplace.service.LostArkApiService;
import com.lostark.marketplace.util.JwtUtil;
import com.lostark.marketplace.util.PasswordGenerator;

class UserServiceImplTest {
  
  @Mock
  private JwtUtil jwtUtil;
  
  @Mock
  private AuthenticationManager authenticationManager;
  
  @Mock
  private PasswordEncoder passwordEncoder;
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private CharacterInfoRepository characterInfoRepository;
  
  @Mock
  private LostArkApiService lostArkApiService;
  
  @InjectMocks
  private UserServiceImpl userService;
  
  private UserEntity testUser;
  private UserDto.SignUp signUpRequest;
  private UserDto.SignIn signInRequest;
  private UserDto.UpdateCurrencyRequest updateCurrencyRequest;
  private UserDto.UpdateEmailRequest updateEmailRequest;
  private UserDto.ChangePasswordRequest changePasswordRequest;
  private String generatedPassword;
  private String encodedPassword;
  
  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    
    // 사용자 테스트 객체 생성
    testUser = UserEntity.builder()
        .username("testUser")
        .password("password123")
        .email("test@example.com")
        .role(UserRole.USER)
        .characterInfos(new ArrayList<>())
        .build();
    
    // 회원가입 요청 테스트 객체 생성
    signUpRequest = UserDto.SignUp.builder()
        .role("USER")
        .username("testUser")
        .password("password123")
        .email("test@example.com")
        .build();
    
    // 로그인 요청 테스트 객체 생성
    signInRequest = UserDto.SignIn.builder()
        .username("testUser")
        .password("password123")
        .build();
    
    // 변경할 재화 정보 요청 테스트 객체 생성
    updateCurrencyRequest = new UserDto.UpdateCurrencyRequest(
        Optional.of(10000),
        Optional.of(5000));
    
    // 변경할 이메일 정보 요청 테스트 객체 생성
    updateEmailRequest = UserDto.UpdateEmailRequest.builder()
        .oldEmail("test@example.com")
        .newEmail("newEmail@example.com")
        .build();
    
    // 변경할 패스워드 정보 요청 테스트 객체 생성
    changePasswordRequest = UserDto.ChangePasswordRequest.builder()
        .oldPassword("password123")
        .newPassword("password456")
        .build();
    
    // 랜덤 비밀번호와 인코딩된 비밀번호 값 설정
    generatedPassword = "newRandomPassword";
    encodedPassword = "newEncodedPassword";
  }
  
  @Test
  @DisplayName("signUp() 성공 케이스 - 새로운 유저 회원가입")
  void signUp_Success() {
    when(this.userRepository.existsByUsername(this.signUpRequest.getUsername())).thenReturn(false);
    when(this.userRepository.existsByEmail(this.signUpRequest.getEmail())).thenReturn(false);
    when(this.passwordEncoder.encode(this.signUpRequest.getPassword())).thenReturn("testPassword");
    
    UserEntity savedUser = this.testUser;
    savedUser.setCreateAt(LocalDateTime.now());
    when(this.userRepository.save(any(UserEntity.class))).thenReturn(savedUser);
    
    UserDto result = this.userService.signUp(signUpRequest);
    
    assertEquals(signUpRequest.getUsername(), result.getUsername());
    assertEquals(signUpRequest.getEmail(), result.getEmail());
    verify(this.userRepository, times(1)).save(any(UserEntity.class));
  }
  
  @Test
  @DisplayName("signUp() 실패 케이스 - 중복된 유저 이름")
  void signUp_Failure_DuplicateUsername() {
    when(this.userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.signUp(signUpRequest));
  }
  
  @Test
  @DisplayName("signUp() 실패 케이스 - 중복된 유저 이메일")
  void signUp_Failure_DuplicateEmail() {
    when(this.userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.signUp(signUpRequest));
  }
  
  @Test
  @DisplayName("signIn() 성공 케이스 - 올바른 유저 정보로 로그인")
  void signIn_Success() {
    Authentication mockAuthentication = mock(Authentication.class);
    when(mockAuthentication.getName()).thenReturn("testUser");
    when(mockAuthentication.getAuthorities()).thenReturn(List.of());
    
    when(this.userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(testUser));
    when(this.passwordEncoder.matches(signInRequest.getPassword(), testUser.getPassword())).thenReturn(true);
    when(this.authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);
    when(this.jwtUtil.createToken(anyString(), any())).thenReturn("dummyToken");
    
    String token = this.userService.signIn(signInRequest);
    
    assertEquals("dummyToken", token);
    verify(this.jwtUtil, times(1)).createToken(anyString(), any());
  }
  
  @Test
  @DisplayName("signIn() 실패 케이스 - 유저 미존재")
  void signIn_Faileure_UserNotFound() {
    when(this.userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.empty());
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.signIn(signInRequest));
  }
  
  @Test
  @DisplayName("signIn() 실패 케이스 - 비밀번호 검증 실패")
  void signIn_Failure_IncorrectPassword() {
    when(this.userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(testUser));
    when(this.passwordEncoder.matches(signInRequest.getPassword(), testUser.getPassword())).thenReturn(false);
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.signIn(signInRequest));
  }
  
  @Test
  @DisplayName("signIn() 실패 케이스 - JWT 생성 실패")
  void signIn_Failure_Authentication() {
    when(this.userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(testUser));
    when(this.passwordEncoder.matches(signInRequest.getPassword(), testUser.getPassword())).thenReturn(true);
    when(this.authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.signIn(signInRequest));
  }
  
  @Test
  @DisplayName("getProfile() 성공 케이스 - 유저 프로필 검색")
  void getProfile_Success() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
    
    UserDto result = this.userService.getProfile("testUser");
    
    assertEquals(testUser.getUsername(), result.getUsername());
  }
  
  @Test
  @DisplayName("getProfile() 실패 케이스 - 유저 미존재")
  void getProfile_Failure_UserNotFound() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.getProfile("testUser"));
  }
  
  @Test
  @DisplayName("updateUserCurrencyByAdmin() 성공 케이스 - 유저 재화 정보 수정")
  void updateUserCurrencyByAdmin_Success() {
    when(this.userRepository.findById(999L)).thenReturn(Optional.of(testUser));
    
    UserDto result = this.userService.updateUserCurrencyByAdmin(999L, updateCurrencyRequest);
    
    assertEquals(10000, result.getGold());
    assertEquals(5000, result.getPoint());
    verify(this.userRepository, times(1)).save(testUser);
  }
  
  @Test
  @DisplayName("updateUserCurrencyByAdmin() 실패 케이스 - 유저 미존재")
  void updateUserCurrencyByAdmin_Failure_UserNotFound() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.updateUserCurrencyByAdmin(999L, updateCurrencyRequest));
  }
  
  @Test
  @DisplayName("updateEmail() 성공 케이스 - 유저 이메일 정보 수정")
  void updateEmail_Success() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
    
    UserDto result = this.userService.updateEmail("testUser", updateEmailRequest);
    
    assertEquals("newEmail@example.com", result.getEmail());
  }
  
  @Test
  @DisplayName("updateEmail() 실패 케이스 - 유저 미존재")
  void updateEmail_Failure_UserNotFound() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.updateEmail("testUser", updateEmailRequest));
  }
  
  @Test
  @DisplayName("updateEmail() 실패 케이스 - 이메일 검증 실패")
  void updateEmail_Failure_IncorrectEmail() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
    
    UserDto.UpdateEmailRequest request = UserDto.UpdateEmailRequest.builder()
        .oldEmail("incorrect@example.com")
        .newEmail("newEmail@example.com"
            ).build();
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.updateEmail("testUser", request));
  }
  
  @Test
  @DisplayName("changePassword() 성공 케이스 - 유저 비밀번호 변경")
  void changePassword_Success() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
    when(this.passwordEncoder.matches(changePasswordRequest.getOldPassword(), testUser.getPassword())).thenReturn(true);
    when(this.passwordEncoder.encode(changePasswordRequest.getNewPassword())).thenReturn("password456");
    
    this.userService.changePassword("testUser", changePasswordRequest);
    
    verify(this.userRepository, times(1)).save(testUser);
    assertEquals("password456", testUser.getPassword());
  }
  
  @Test
  @DisplayName("changePassword() 실패 케이스 - 유저 미존재")
  void changePassword_Failure_UserNotFound() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.empty());
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.changePassword("testUser", changePasswordRequest));
  }
  
  @Test
  @DisplayName("changePassword() 실패 케이스 - 기존 비밀번호 검증 실패")
  void changePassword_Failure_IncorrectOldPassword() {
    when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
    when(this.passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(false);
    
    assertThrows(LostArkMarketplaceException.class, () -> this.userService.changePassword("testUser", changePasswordRequest));
  }
  
  @Test
  @DisplayName("resetPassword() 성공 케이스 - 비밀번호 초기화 후 변경")
  void resetPassword_Success() {
    try (MockedStatic<PasswordGenerator> mockedPasswordGenerator = mockStatic(PasswordGenerator.class)) {
      when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));
      when(PasswordGenerator.generateRandomPassword(16)).thenReturn(generatedPassword);
      when(this.passwordEncoder.encode(generatedPassword)).thenReturn(encodedPassword);
      
      String result = this.userService.resetPassword(testUser.getUsername());
      
      assertEquals(generatedPassword, result);
      assertEquals(encodedPassword, testUser.getPassword());
      verify(userRepository, times(1)).save(testUser);
    }
  }
  
  @Test
  @DisplayName("resetPassword() 실패 케이스 - 유저 미존재")
  void resetPassword_Failure_UserNotFound() {
    try (MockedStatic<PasswordGenerator> mockedPasswordGenerator = mockStatic(PasswordGenerator.class)) {
      when(this.userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

      assertThrows(LostArkMarketplaceException.class, () -> this.userService.resetPassword("testUser"));
    }
  }
  
  @Test
  @DisplayName("resetPassword() 실패 케이스 - 랜덤 비밀번호 생성 실패")
  void resetPassword_Failure_PasswordGenerationException() {
    try (MockedStatic<PasswordGenerator> mockedPasswordGenerator = mockStatic(PasswordGenerator.class)) {
      when(this.userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
      
      mockedPasswordGenerator.when(() -> PasswordGenerator.generateRandomPassword(16))
          .thenThrow(new RuntimeException("비밀번호 생성 실패"));
      
      assertThrows(RuntimeException.class, () -> this.userService.resetPassword("testUser"));
    }
  }
  
}
