package com.lostark.marketplace.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.lostark.marketplace.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(LostArkMarketplaceException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(LostArkMarketplaceException e, HttpServletRequest request) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(e.getTimestamp())
        .message(e.getHttpStatusCode().getMessage())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(e.getHttpStatusCode().getStatus()).body(errorResponse);
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, String> fieldErrors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.put(error.getField(), error.getDefaultMessage());
    }
    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .message("Validation Failed")
        .path(request.getRequestURI())
        .fieldErrors(fieldErrors)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
  
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .path(request.getRequestURI())
        .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
  
}
