package com.lostark.marketplace.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import com.lostark.marketplace.model.CharacterInfoDto;
import com.lostark.marketplace.model.UserDto;
import com.lostark.marketplace.model.constant.LostArkClass;
import com.lostark.marketplace.model.constant.UserRole;
import com.lostark.marketplace.persist.CharacterInfoRepository;
import com.lostark.marketplace.persist.UserRepository;
import com.lostark.marketplace.persist.entity.CartEntity;
import com.lostark.marketplace.persist.entity.CharacterInfoEntity;
import com.lostark.marketplace.persist.entity.UserEntity;
import com.lostark.marketplace.service.LostArkApiService;
import com.lostark.marketplace.service.PointService;
import com.lostark.marketplace.service.UserService;
import com.lostark.marketplace.util.JwtUtil;
import com.lostark.marketplace.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  
  private final JwtUtil jwtUtil;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final CharacterInfoRepository characterInfoRepository;
  private final LostArkApiService lostArkApiService;
  private final PointService pointService;
  
  private final int passwordLength = 16;
  
  @Override
  public UserDto signUp(UserDto.SignUp request) {
    // 사용자 이름 중복 체크
    if (this.userRepository.existsByUsername(request.getUsername())) {
      throw new LostArkMarketplaceException(HttpStatusCode.CONFLICT);
    }
    
    // 사용자 이메일 중복 체크
    if (this.userRepository.existsByEmail(request.getEmail())) {
      throw new LostArkMarketplaceException(HttpStatusCode.CONFLICT);
    }
    
    // 신규 유저 정보 생성
    UserEntity user = UserEntity.builder()
        .role(UserRole.valueOf(request.getRole()))
        .username(request.getUsername())
        .password(this.passwordEncoder.encode(request.getPassword()))
        .email(request.getEmail())
        .gold(0)
        .point(0)
        .createAt(LocalDateTime.now())
        .characterInfos(new ArrayList<>())
        .inventory(new HashSet<>())
        .build();
    
    // 장바구니 생성
    CartEntity cart = CartEntity.builder()
        .user(user)
        .orders(new ArrayList<>())
        .build();
    
    // 신규 유저 정보에 장바구니 설정
    user.setCart(cart);
    
    // 신규 유저 정보 저장
    this.userRepository.save(user);
    
    return user.toDto();
  }
  
  @Override
  public String signIn(UserDto.SignIn request) {
    // 유저 조회
    UserEntity user = this.userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 비밀번호 검증
    if (!this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new LostArkMarketplaceException(HttpStatusCode.UNAUTHORIZED);
    }
    
    // 일일 로그인 보상 지급
    this.pointService.awardDailyLoginBonus(user);
    
    // 인증 시도 후 성공 시 JWT 반환
    try {
      Authentication authentication = this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      
      return this.jwtUtil.createToken(authentication.getName(), authentication.getAuthorities());
    } catch (AuthenticationException e) {
      throw new LostArkMarketplaceException(HttpStatusCode.UNAUTHORIZED);
    }
  }
  
  @Override
  public UserDto getProfile(String username) {
    // 유저 조회 후 반환
    UserEntity user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    return user.toDto();
  }
  
  @Override
  public UserDto updateUserCurrencyByAdmin(Long userId, UserDto.UpdateCurrencyRequest request) {
    // 유저 조회
    UserEntity user = this.userRepository.findById(userId)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // ifPresent() 메소드로 값이 있을 때만 엔티티에 반영
    request.getGold().ifPresent(user::setGold);
    request.getPoint().ifPresent(user::setPoint);
    
    return user.toDto();
  }

  @Override
  public UserDto updateEmail(String username, UserDto.UpdateEmailRequest request) {
    // 유저 조회
    UserEntity user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 변경 전 이메일 검증
    if (!user.getEmail().equals(request.getOldEmail())) {
      throw new LostArkMarketplaceException(HttpStatusCode.BAD_REQUEST);
    }
    
    // 변경할 이메일 중복 체크
    if (this.userRepository.existsByEmail(request.getNewEmail())) {
      throw new LostArkMarketplaceException(HttpStatusCode.CONFLICT);
    }
    
    // 이메일 변경
    user.setEmail(request.getNewEmail());
    
    return user.toDto();
  }

  @Override
  public void changePassword(String username, UserDto.ChangePasswordRequest request) {
    // 유저 조회
    UserEntity user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 기존 비밀번호 검증
    if (!this.passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new LostArkMarketplaceException(HttpStatusCode.UNAUTHORIZED);
    }
    
    // 새로운 비밀번호 설정
    user.setPassword(this.passwordEncoder.encode(request.getNewPassword()));
  }
  
  @Override
  public String resetPassword(String username) {
    // 유저 조회
    UserEntity user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 랜덤 비밀번호 생성 로직
    String newPassword = PasswordGenerator.generateRandomPassword(this.passwordLength);
    
    // 초기화된 비밀번호 설정
    user.setPassword(this.passwordEncoder.encode(newPassword));
    
    return newPassword;
  }
  
  @Override
  public UserDto syncLostArkCharacters(String username, String characterName) {
    // 유저 조회
    UserEntity user = this.userRepository.findByUsername(username)
        .orElseThrow(() -> new LostArkMarketplaceException(HttpStatusCode.NOT_FOUND));
    
    // 캐릭터 연동 - LostArk Open API에서 캐릭터 데이터 가져오기
    List<CharacterInfoDto> characterDatas = this.lostArkApiService.getCharacterData(characterName);
    
    // CharacterInfoDto 리스트를 CharacterInfoEntity 리스트로 변환
    List<CharacterInfoEntity> characterEntities = characterDatas.stream()
        .map(dto -> CharacterInfoEntity.builder()
            .serverName(dto.getServerName())
            .characterName(dto.getCharacterName())
            .characterLevel(dto.getCharacterLevel())
            .characterClassName(LostArkClass.valueOf(dto.getCharacterClassName()))
            .itemAvgLevel(dto.getItemAvgLevel())
            .itemMaxLevel(dto.getItemMaxLevel())
            .user(user)
            .build())
        .collect(Collectors.toList());
    
    // 캐릭터 리스트 저장
    for (CharacterInfoEntity character : characterEntities) {
      this.characterInfoRepository.save(character);
    }
    
    // 캐릭터 리스트 갱신
    user.setCharacterInfos(characterEntities);
    
    // 캐릭터 연동 정보 저장
    this.userRepository.save(user);
    
    return user.toDto();
  }
  
}
