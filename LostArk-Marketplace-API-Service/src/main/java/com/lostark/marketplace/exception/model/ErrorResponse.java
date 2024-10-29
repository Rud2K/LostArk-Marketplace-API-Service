package com.lostark.marketplace.exception.model;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
  
  private LocalDateTime timestamp; // 에러 발생 시각
  
  private String message; // 에러 메시지
  
  private String path; // 에러 발생 경로
  
  private Map<String, String> fieldErrors; // 필드 유효성 검사 에러
  
}
