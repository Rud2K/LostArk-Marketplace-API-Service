package com.lostark.marketplace.util;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.lostark.marketplace.persist.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  
  private final UserRepository userRepository;
  
  /**
   * 주어진 사용자 이름을 기반으로 사용자 정보를 로드하는 메소드
   * 
   * @param username 사용자 이름
   * @return UserDetails 사용자 정보
   * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
  }
  
}

