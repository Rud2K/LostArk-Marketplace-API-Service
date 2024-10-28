package com.lostark.marketplace.util;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
  
  private static final String KEY_ROLES = "roles";
  
  @Value("${jwt.secret}")
  private String secretKey;
  
  @Value("${jwt.expiration}")
  private Long expirationTime;
  
  private Key key;
  
  /**
   * 비밀 키를 사용하여 HMAC 키를 초기화하는 메소드
   * 
   * @PostConstruct 어노테이션을 통해 Spring 컨테이너가 생성된 후 자동으로 호출됨
   */
  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(this.secretKey.getBytes());
  }
  
  /**
   * JWT를 생성하는 메소드
   * 
   * @param username 사용자 이름
   * @param authorities 사용자 권한
   * @return 생성된 JWT
   */
  public String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
    // 클레임에 사용자 이름 설정
    Claims claims = Jwts.claims().setSubject(username);
    
    // 클레임에 권한 추가
    claims.put(KEY_ROLES, authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList()));
    
    // 현재 시간과 만료 시간 설정
    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + this.expirationTime);
    
    // JWT 생성
    try {
      return Jwts.builder()
          .setClaims(claims) // 클레임 설정
          .setIssuedAt(now) // 발급 시간 설정
          .setExpiration(expiredDate) // 만료 시간 설정
          .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘 설정
          .compact(); // 압축
    } catch (Exception e) {
      throw new RuntimeException("JWT 생성 오류");
    }
  }
  
  /**
   * JWT를 파싱해 사용자 이름을 추출하는 메소드
   * 
   * @param token JWT
   * @return 추출된 사용자 이름
   */
  public String getUsernameFromToken(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(this.key).build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (JwtException e) {
      throw new RuntimeException("JWT 파싱 오류");
    }
  }
  
  /**
   * JWT의 유효성을 검증하는 메소드
   * 
   * @param token JWT
   * @return JWT의 서명 및 만료 여부
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      throw new RuntimeException("잘못된 JWT 서명 또는 형식 오류");
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("만료된 JWT 토큰");
    } catch (UnsupportedJwtException e) {
      throw new RuntimeException("지원되지 않는 JWT 토큰");
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("JWT 토큰이 비어 있거나 잘못된 입력");
    }
  }
  
}
