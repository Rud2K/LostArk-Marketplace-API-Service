# LostArk Marketplace API Service

로스트아크 오픈 API 연동 이커머스 서비스

## 📌 프로젝트 개요

이 프로젝트는 **로스트아크 오픈 API**와 통합 이커머스 API 서비스를 제공합니다.

유저는 자신의 캐릭터 정보를 연동해 게임 내 재화로 아이템을 구매하거나 보너스 혜택을 지급받을 수 있습니다.

## 🛠 기능 설명

### 1️⃣ 회원 정보 (필수 사항)

**설명**

- 유저는 로스트아크 캐릭터 정보를 연동하여 회원 정보를 관리합니다.

**세부 기능**

- **프로필 조회 및 수정**:
    - `Spring Data JPA`를 활용해 유저 정보 CRUD 수행.
    - 비밀번호 변경 시 `BCrypt` 암호화 적용.
    - `JWT`를 통한 인증으로 개인정보 접근 제어.
- **로스트아크 캐릭터 연동**:
    - 로스트아크 오픈 API로 캐릭터 목록 및 능력치를 불러와 저장.
- **구매 및 결제 내역 조회**:
    - DB와 연동해 `Spring Data JPA`로 결제 이력 관리.
    - 페이징 처리를 적용해 내역을 분할 조회.
- **비밀번호 초기화**:
    - 랜덤한 비밀번호 생성 후 응답 반환 및 `Spring Data JPA`를 통해 DB에 반영.

### 2️⃣ Market 정보 실시간 갱신 (필수 사항)

**설명**

- 로스트아크 오픈 API의 Market 정보를 5분마다 주기적으로 호출하여 DB에 저장합니다.

**세부 기능**

- **DB 저장**:
    - `Spring Scheduler` 를 사용해 5분마다 로스트아크 오픈 API에서 최신 Market 정보를 불러와 DB에 저장.
- **데이터 응답 최적화**:
    - 클라이언트 요청 시마다 DB에서 데이터를 조회하여 최신 상태를 유지합니다.

### 3️⃣ 상품명 검색 기능 (필수 사항)

**설명**

- Market에서 구매하려는 상품을 검색할 수 있습니다.

**세부 기능**

- **상품명 자동 완성**:
    - **Elasticsearch**의 강력한 검색 기능을 활용하여 입력된 검색어와 일치하는 상품명을 빠르게 추천.
    - 유사도 기반 결과 제공.
    - 실시간으로 Market 데이터를 Elasticsearch에 저장하여 자동 완성 기능을 최적화.
- **필터 기능**:
    - `Specification`과 동적 `LIKE` 쿼리를 사용해 상품명을 포함하여 상품명, 등급, 가격 조건으로 검색 필터링을 지원.
    - `Page`와 `Pageable` 객체를 통해 자동 완성 결과를 페이지 단위로 제공.

### 4️⃣ 인게임 재화 및 포인트로 구매 기능 (필수 사항)

**설명**

- 게임에서 획득한 인게임 재화(골드)와 포인트를 활용해 이커머스 Market에서 아이템을 구매합니다.

**세부 기능**

- **골드 결제**:
    - 유저의 잔여 골드를 확인하고 결제 처리.
    - **골드 부족 시** 결제 실패 메시지를 전달.
- **구매 확정 시 인벤토리 반영**:
    - 결제 완료 후 유저의 인벤토리에 상품 정보 반영.
    - 재고 관리 및 동시성 문제 해결을 위한 **락(Lock) 처리**.
- **환불 기능**:
    - 구매 취소 시 구매 이력을 확인하여 환불 처리.
- **결제 시 포인트 적립**:
    - `Spring Event Listener`를 사용해 결제 성공 시 포인트 적립 이벤트 트리거.

### 5️⃣ 장바구니 기능 (필수 사항)

**설명**

- 유저가 상품을 장바구니에 담아 관리하고 결제할 수 있습니다.

**세부 기능**

- **상품 담기**:
    - 동일 상품 선택 시 수량이 자동 증가.
- **수량 변경 및 삭제**:
    - 장바구니의 수량 조정 및 삭제 처리.
- **장바구니 조회**:
    - 장바구니 총 가격과 결제에 필요한 재화를 계산해 제공.
- **장바구니 결제**:
    - 결제 시 DB에 구매 이력을 저장.

### 6️⃣ 포인트 적립 기능 (선택 사항)

**설명**

- 유저가 결제한 금액에 따라 포인트를 적립해 추가 혜택을 제공합니다.

**세부 기능**

- **일반 포인트 적립**:
    - 결제 금액의 3%를 포인트로 적립.
    - `Spring Event Listener`로 포인트 적립 트리거 발생.
- **특정 레벨 이상 시 차등 적립**:
    - 특정 레벨 이상은 최대 10%까지 차등 적립.
    - 로스트아크 오픈 API로 특정 레벨 조건 확인.
- **포인트 유효 기간**:
    - 6개월 내 사용 제한.
- **포인트 회수**:
    - **구매 취소 시** 적립된 포인트는 회수. 단, 이미 사용한 포인트는 차감하지 않음.

## 🧑‍💻 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 3.3.4
- **Build Tool**: Gradle
- **Database**:
    - **Production**: MariaDB
    - **Development**: H2 Database
    - **Search Engine**: Elasticsearch
- **ORM**: JPA (Java Persistence API)
- **Library**:
    - Lombok
    - Hibernate Validator
    - Apache Commons Collections 4
    - JPA Specification
- **Security**:
    - Spring Security
    - JWT (JSON Web Token)
- **Batch Processing**: Spring Scheduler (Batch 작업으로 Market 정보 주기적 갱신)
- **Event Handling**: Spring Event Listener (포인트 적립 및 기타 이벤트 처리)
- **Test**: Junit5
- **Server**: Embedded Tomcat
- **Open API**: LostArk Open API (캐릭터, 거래소 정보 연동)
- **IDE**: Eclipse

## 📂 프로젝트 구조

1. **회원 정보 관리**: 유저의 로스트아크 계정과 프로필 관리
2. **Market 정보 실시간 갱신**: Spring Scheduler를 사용해 Market 데이터 실시간 반영
3. **상품 검색 기능**: Elasticsearch를 통한 실시간 자동 완성, 필터링을 통한 상품 탐색
4. **인게임 재화 및 포인트로 구매 기능**: 재화 결제 및 인벤토리 반영
5. **장바구니 기능**: 상품 추가, 조회 및 결제 처리
6. **포인트 적립 및 보상**: 결제 금액에 따른 포인트 제공

## 💾 ERD
<img src="https://github.com/user-attachments/assets/9cb83e4e-7600-42d8-bfce-a2bdc491b9f2" width="800" height="550"/>  

## 🔗 API 연동 정보  
- **LostArk Open API**를 활용해 **캐릭터 정보**와 **거래소 정보**를 불러옵니다.  

## 🛠️ Trouble Shooting
<a href="https://www.notion.so/Trouble-Shooting-124518e1879e801da845df0266401597">
    <img src="https://skillicons.dev/icons?i=notion" />
</a>
