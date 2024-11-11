package com.lostark.marketplace.persist.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.lostark.marketplace.model.UserDto;
import com.lostark.marketplace.model.constant.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class UserEntity implements UserDetails {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId; // 고유 식별자 (자동 생성)
  
  @Enumerated(EnumType.STRING)
  private UserRole role; // 유저 권한
  
  private String username; // 유저 이름
  
  private String password; // 유저 비밀번호
  
  private String email; // 유저 이메일
  
  private Integer gold; // 유저가 보유 중인 인게임 재화(골드)
  
  private Integer point; // 유저가 보유 중인 포인트
  
  private Integer couponCount; // 유저가 보유 중인 쿠폰의 개수
  
  private LocalDateTime createAt; // 생성 일자
  
  @OneToMany(mappedBy = "user")
  private List<CharacterInfoEntity> characterInfos; // 유저가 보유 중인 캐릭터 목록
  
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private CartEntity cart; // 유저의 장바구니
  
  /**
   * UserEntity를 UserDto로 변환하는 메소드
   * 
   * @return UserDto 객체
   */
  public UserDto toDto() {
    return UserDto.builder()
        .userId(this.userId)
        .role(this.role.name())
        .username(this.username)
        .email(this.email)
        .gold(this.gold)
        .point(this.point)
        .couponCount(this.couponCount)
        .createAt(this.createAt)
        .characterInfos(this.characterInfos.stream()
            .map(CharacterInfoEntity::toDto)
            .collect(Collectors.toList()))
        .build();
  }
  
  /**
   * 사용자의 권한을 반환하는 메소드
   * 
   * @return 사용자의 권한
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
  }
  
}
