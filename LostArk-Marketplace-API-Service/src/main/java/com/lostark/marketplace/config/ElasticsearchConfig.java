package com.lostark.marketplace.config;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;
import com.lostark.marketplace.exception.LostArkMarketplaceException;
import com.lostark.marketplace.exception.model.HttpStatusCode;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {
  
  @Value("${spring.elasticsearch.uris}")
  private String elasticsearchUrl;
  
  @Value("${spring.elasticsearch.api-key}")
  private String apiKey;
  
  @Override
  public ClientConfiguration clientConfiguration() {
    try {
      // SSL 인증서를 무시하도록 SSLContext 생성
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, new TrustManager[] {new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { return null; }
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
      }}, new SecureRandom());
      
      // org.springframework.data.elasticsearch.support.HttpHeaders 사용
      HttpHeaders headers = new HttpHeaders();
      headers.add("Authorization", "ApiKey " + this.apiKey);
      
      return ClientConfiguration.builder()
          .connectedTo(this.elasticsearchUrl.replace("https://", ""))
          .usingSsl(sslContext)
          .withDefaultHeaders(headers)
          .build();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new LostArkMarketplaceException(HttpStatusCode.SERVICE_UNAVAILABLE);
    }
  }
  
}
