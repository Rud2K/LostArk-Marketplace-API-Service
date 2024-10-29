package com.lostark.marketplace.exception;

import java.time.LocalDateTime;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class LostArkMarketplaceException extends RuntimeException {
  
  private final LocalDateTime timestamp;
  private final HttpStatusCode httpStatusCode;
  
  public LostArkMarketplaceException(HttpStatusCode httpStatusCode) {
    super(httpStatusCode.getMessage());
    this.timestamp = LocalDateTime.now();
    this.httpStatusCode = httpStatusCode;
  }
  
}
