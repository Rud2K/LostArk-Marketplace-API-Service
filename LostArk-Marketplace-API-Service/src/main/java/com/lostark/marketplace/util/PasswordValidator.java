package com.lostark.marketplace.util;

import com.lostark.marketplace.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
  
  private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{1,15}$";
  
  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    // 빈 문자열과 null 체크
    if (password.isEmpty() || password == null) {
      return false;
    }
    
    // 비밀번호 길이 체크 (16자)
    if (password.length() > 16 || password.length() < 0) {
      return false;
    }
    
    // 특수 문자, 영어 대소문자, 숫자 조합 체크
    if (!password.matches(PASSWORD_PATTERN)) {
      return false;
    }
    
    // 반복적인 문자 패턴 체크 (예: aaaa, bbb 등)
    if (this.containsRepeatedCharacters(password, 3)) {
      return false;
    }
    
    // 반복적인 숫자 패턴 체크 (예: 1111, 222 등)
    return !this.containsRepeatedNumbers(password, 3);
  }
  
  private boolean containsRepeatedCharacters(String password, int maxRepeat) {
    return password.matches(".*(.)\\1{" + (maxRepeat - 1) + ",}.*");
  }
  
  private boolean containsRepeatedNumbers(String password, int maxRepeat) {
    return password.matches(".*(\\d)\\1{" + (maxRepeat - 1) + ",}.*");
  }
  
}
