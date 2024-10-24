## 🛠️ 작업 내용 (What)
### 변경된 작업 내용을 간단하게 작성
- 주요 변경 사항

<br>

## 📌 작업 이유 (Why)
### 이 작업이 왜 필요한지 설명
- 변경이 필요한 이유

<br>

## ✨ 변경 사항 (Changes)
- 주요 기능 추가/수정 내용
- 관련 모듈 설명
- 로직 변경 사항 요약

<br>

## ✅ 테스트 (Tests)
- [ ] 유닛 테스트가 통과하였음
- [ ] 기능 테스트가 통과하였음
- 테스트 항목:
  - 추가된 테스트 항목에 대한 간단한 설명
  - 테스트 방식 및 사용한 테스트 도구

<br>

## 💬 리뷰 포인트 (Review Points)
- PR시 참고할 사항들이나 중요하게 볼 포인트들에 대한 설명

<br>

## ⚙️ 설정 파일 (application.yml)
이 프로젝트는 `application.yml` 파일을 Git에 포함하지 않습니다.
프로젝트를 실행하기 위해서는 다음과 같이 설정 파일을 로컬에 추가해야 합니다.

1. `src/main/resources/` 디렉토리 안에 `application.yml` 파일을 생성합니다.
2. 아래 예시와 같이 설정합니다.

```yaml
# 공통 설정
# HTTPS 설정 (HTTPS를 사용하지 않으려면 주석 처리하고 아래 HTTP 포트를 활성화하세요)
server:
  port: 8443
  ssl:
    enabled: true
    key-store: your_local_key-store_path
    key-store-password: your_key-store_password
    key-store-type: PKCS12
    key-alias: localcert

# HTTP 설정 (HTTPS를 사용하지 않을 경우 활성화)
server:
  port: 8080
  
jwt:
  secret: 685a129e8376b97b47b68c742dbfb2052ec38e7a9ff456369703e89b8e62219d
  expiration: 3600000
  
lostark:
  api-key: eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDA1NjQ4MTAifQ.iC4hcATlcDqkeQcim3-pdoC1sKRZx4fGufPZHBKGA7-uCArfxlT-LtOhSB5iKZWUPBmg0RfxGGrUNC64PQABb8cRNekAq5exgAhq1FluJf02St2JzLTIqElUTXiiwgaOJcScTIYqjxAex4jHuZ6nFevdXFxUv2pn_Ql2a32RWA_RSz-NEA93VBwQEk3Ch2lLOyWfjI4iNc8_2p-5m3TmuKxalsRH2GGICQidZQ7qK_U7rt0p6zZ4FTtpAIh_zSscdQJo7yiAYB5Xv-m1q2SBOLv2wY9A9hv-itzTpU_ZLTF_F3aTaZfjQBsInGVYAj8DVk31_mXUGf8VVwmHpqAGgA
  base-url: https://developer-lostark.game.onstove.com

# dev 프로파일: H2 데이터베이스 사용
spring:
  profiles:
    active: dev

  datasource:
    url: jdbc:h2:mem:test
    driver-class-name: org.h2.Driver
    username: sa
    password: 
    
  h2:
    console:
      enabled: true
      path: /h2-console
      
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```
