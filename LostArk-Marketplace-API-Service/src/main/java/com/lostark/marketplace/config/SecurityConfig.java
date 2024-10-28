package com.lostark.marketplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.lostark.marketplace.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  
  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;
  
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
  
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // CSRF 비활성화
        .csrf(csrf -> csrf.disable())
        
        // 세션 관리: JWT 기반 인증이므로 세션을 사용하지 않도록 설정
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        
        // 요청에 대한 인증 설정
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/user/signup", "/user/signin").permitAll()
            .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
        )
        
        // JWT 필터 추가
        .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        
        // Content Security Policy 설정
        .headers(headers -> headers
            .contentSecurityPolicy(csp -> csp.policyDirectives("frame-ancestors 'self'")))
        
        // 기본 폼 로그인 및 HTTP Basic 인증 비활성화
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable());
    
    return http.build();
  }
  
}
