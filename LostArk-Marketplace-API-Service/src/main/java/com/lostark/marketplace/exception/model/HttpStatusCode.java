package com.lostark.marketplace.exception.model;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum HttpStatusCode {
  
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."), // 400
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."), // 401
  FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."), // 403
  NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."), // 404
  CONFLICT(HttpStatus.CONFLICT, "리소스 충돌이 발생했습니다."), // 409
  CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."), // 201
  NO_CONTENT(HttpStatus.NO_CONTENT, "요청이 성공했지만 반환할 내용이 없습니다."), // 204
  BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "잘못된 게이트웨이 요청입니다."), // 502
  SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "현재 서버를 사용할 수 없습니다."), // 503
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."); // 500
  
  private final HttpStatus status;
  private final String message;
  
  HttpStatusCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
  
  /**
   * 메시지에 변수를 넣어 동적으로 메시지 생성
   * 
   * @param args 메시지에 들어갈 변수들
   * @return 포맷된 메시지
   */
  public String formatMessage(Object... args) {
    return String.format(this.message, args);
  }
  
}
