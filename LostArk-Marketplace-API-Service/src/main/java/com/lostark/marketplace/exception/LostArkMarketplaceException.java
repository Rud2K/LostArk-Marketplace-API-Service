package com.lostark.marketplace.exception;

import java.time.LocalDateTime;
import com.lostark.marketplace.exception.model.HttpStatusCode;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class LostArkMarketplaceException extends RuntimeException {
  
  private final LocalDateTime timestamp;
  private final int status;
  private final HttpStatusCode httpStatusCode;
  private final String path;
  
  public LostArkMarketplaceException(HttpStatusCode httpStatusCode, String path) {
    super(httpStatusCode.getMessage());
    this.timestamp = LocalDateTime.now();
    this.status = httpStatusCode.getStatus().value();
    this.httpStatusCode = httpStatusCode;
    this.path = path;
  }
  
}
