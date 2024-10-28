package com.lostark.marketplace.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
  
  // 현재 요청을 전역적으로 가져오는 메소드
  public static HttpServletRequest getCurrentRequest() {
    return ((ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes())
        .getRequest();
  }
  
}
