package com.lostark.marketplace.util;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  private static final String TOKEN_HEADER = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";
  
  private final CustomUserDetailsService customUserDetailsService;
  private final JwtUtil jwtUtil;
  
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    // 요청에서 JWT를 추출
    String token = this.getTokenFromRequest(request);
    
    // 토큰이 존재하고 유효한 경우
    if (StringUtils.hasText(token) && this.jwtUtil.validateToken(token)) {
      // 토큰에서 사용자 이름 추출
      String username = this.jwtUtil.getUsernameFromToken(token);
      
      // 사용자 세부 정보 로드
      UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
      
      // 인증 객체 생성
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      
      // 요청의 세부정보 설정
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      
      // SecurityContext에 인증 정보 설정
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    // 다음 필터로 요청 및 응답 정보 전달
    filterChain.doFilter(request, response);
  }
  
  /**
   * 요청 헤더에서 Bearer 토큰을 추출하는 메소드
   * 
   * @param request HttpServletRequest
   * @return JWT 토큰 (존재하지 않으면 null)
   */
  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader(TOKEN_HEADER);
    // Bearer 토큰이 존재하고, "Bearer "로 시작하면 토큰 반환
    return (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX))
        ? bearerToken.substring(TOKEN_PREFIX.length())
        : null;
  }
  
}
