package com.lostark.marketplace.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.lostark.marketplace.util.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
  String message() default "비밀번호는 특수문자, 숫자, 영어 문자를 포함하며, 반복적인 문자/숫자가 3번 이상 사용될 수 없습니다.";
  
  Class<?>[] groups() default {};
  
  Class<? extends Payload>[] payload() default {};
}
