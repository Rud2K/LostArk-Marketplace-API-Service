package com.lostark.marketplace.util;

import java.security.SecureRandom;

public class PasswordGenerator {
  
  private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
  private static final String DIGITS = "0123456789";
  private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{};:,.<>?";
  
  private static final SecureRandom secureRandom = new SecureRandom();
  
  /**
   * 랜덤 비밀번호 생성
   * 
   * @param passwordLength 비밀번호 길이
   * @param requestURI 클라이언트 요청 URI
   * @return 랜덤하게 생성된 비밀번호
   */
  public static String generateRandomPassword(final int passwordLength) {
    StringBuilder password = new StringBuilder();
    
    // 각 카테고리에서 최소 하나씩 선택
    password.append(UPPER_CASE.charAt(secureRandom.nextInt(UPPER_CASE.length())));
    password.append(LOWER_CASE.charAt(secureRandom.nextInt(LOWER_CASE.length())));
    password.append(DIGITS.charAt(secureRandom.nextInt(DIGITS.length())));
    password.append(SPECIAL_CHARACTERS.charAt(secureRandom.nextInt(SPECIAL_CHARACTERS.length())));
    
    // 남은 길이를 랜덤한 문자로 채움
    String combinedChars = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARACTERS;
    for (int i = 4; i < passwordLength; i++) {
      password.append(combinedChars.charAt(secureRandom.nextInt(combinedChars.length())));
    }
    
    return password.toString();
  }

}
