package com.lostark.marketplace.service;

import com.lostark.marketplace.model.UserDto;

public interface UserService {
  
  // 회원가입 및 로그인 기능
  
  /**
   * 유저 회원가입을 처리하는 메소드
   * 
   * @param request 회원가입 요청 정보
   * @return 등록된 유저 정보
   */
  UserDto signUp(UserDto.SignUp request);
  
  /**
   * 유저 로그인을 처리하는 메소드
   * 
   * @param request 로그인 요청 정보
   * @return 생성된 JWT 정보
   */
  String signIn(UserDto.SignIn request);
  
  
  // 프로필 관리 기능
  
  /**
   * 현재 로그인된 유저의 프로필 조회
   * 
   * @param username 로그인된 유저의 이름
   * @return 유저 정보 (프로필)
   */
  UserDto getProfile(String username);
  
  /**
   * 관리자가 특정 유저의 재화 정보 수정
   * 
   * @param userId 수정할 유저의 ID
   * @param request 수정할 유저 재화 정보를 담은 UserDto 객체
   * @return 수정된 유저 정보
   */
  UserDto updateUserCurrencyByAdmin(Long userId, UserDto.UpdateCurrencyRequest request);
  
  /**
   * 유저의 이메일 변경
   * 
   * @param username 유저의 이름
   * @param request 수정할 유저 이메일 정보를 담은 UserDto 객체
   * @return 수정된 유저 정보
   */
  UserDto updateEmail(String username, UserDto.UpdateEmailRequest request);
  
  /**
   * 유저의 비밀번호 변경
   * 
   * @param username 유저의 이름
   * @param request 수정할 유저 비밀번호 정보를 담은 UserDto 객체
   */
  void changePassword(String username, UserDto.ChangePasswordRequest request);
  
  /**
   * 유저의 비밀번호 초기화
   * 
   * @param username 유저의 이름
   * @return 초기화된 비밀번호
   */
  String resetPassword(String username);
  
  
  // 로스트아크 캐릭터 연동 기능
  
  /**
   * 로스트아크 캐릭터 정보를 연동하여 불러오기
   * 
   * @param username 캐릭터 이름
   * @param characterName 캐릭터 이름
   * @return 연동이 완료된 유저 정보
   */
  UserDto syncLostArkCharacters(String username, String characterName);
  
  
  // 구매 및 결제 내역 조회 기능
  
  /**
   * 사용자의 구매 및 결제 내역 조회
   * 
   * @param userId 사용자 ID
   * @param page 페이지 번호 (페이징 처리)
   * @param size 한 페이지에 표시할 내역 수
   * @return 페이징 처리된 결제 내역 리스트
   */
  // List<PurchaseHistoryDto> getPurchaseHistory(Long userId, int page, int size);
  
  
  // 포인트 관리 기능
  
  /**
   * 사용자의 포인트 적립 및 조회 내역
   * 
   * @param userId 사용자 ID
   * @param page 페이지 번호 (페이징 처리)
   * @param size 한 페이지에 표시할 내역 수
   * @return 포인트 적립 및 사용 내역 리스트
   */
  // List<PointHistoryDto> getPointHistory(Long userId, int page, int size);
  
  
  // 인벤토리 관리 기능
  
  /**
   * 사용자의 인벤토리 조회
   * 
   * @param userId 사용자 ID
   * @return 사용자의 아이템 목록 (인벤토리)
   */
  // List<InventoryDto> getInventory(Long userId);
  
  /**
   * 사용자의 인벤토리에 아이템 추가
   * 
   * @param userId 사용자 ID
   * @param itemId 추가할 아이템의 ID
   * @param quantity 아이템 수량
   */
  // void addItemToInventory(Long userId, Long itemId, int quantity);
  
}
