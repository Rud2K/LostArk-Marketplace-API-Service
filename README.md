# LostArk Marketplace API Service

로스트아크 오픈 API 연동 이커머스 서비스

## 📌 프로젝트 개요

이 프로젝트는 **로스트아크 오픈 API**와 통합 이커머스 API 서비스를 제공합니다.
유저는 자신의 캐릭터 정보를 연동해 게임 내 재화로 아이템을 구매하거나, 특정 랭킹 및 업적 달성 시 보너스 혜택을 지급받을 수 있습니다.

## 🛠 기능 설명

### 1️⃣ 회원정보

**설명**

- 유저는 로스트아크 계정을 연동하여 캐릭터 정보를 관리합니다.

**세부 기능**

- **프로필 조회 및 수정**:

    - Spring Data JPA를 활용해 유저 정보 CRUD 수행.

    - 비밀번호 변경 시 BCrypt 암호화 적용.

    - JWT를 통한 인증으로 개인정보 접근 제어.

- **로스트아크 캐릭터 연동**:

    - 로스트아크 오픈 API로 캐릭터 목록 및 능력치를 불러와 저장.

    - Redis 캐시에 캐릭터 정보를 보관해 호출 부하 감소.

- **구매 및 결제 내역 조회**:

    - MariaDB와 연동해 JPA로 결제 이력 관리.

    - 페이징 처리를 적용해 내역을 분할 조회.

- **비밀번호 초기화**:

    - 랜덤한 비밀번호 생성 후 응답 반환 및 JPA를 통해 DB에 반영.

### 2️⃣ Store 정보 실시간 갱신

**설명**

- 로스트아크 오픈 API의 Store 정보를 실시간으로 반영하면서도 불필요한 API 호출을 줄이기 위해 Redis Cache와 Batch 작업을 조합해 사용합니다.

**세부 기능**

- **Redis Cache 저장**:

    - 로스트아크 오픈 API에서 불러온 Store 정보를 Redis에 일정 시간(5분) 동안 캐시.

- **Batch 작업**:

    - Spring Scheduler로 5분마다 Batch 작업을 실행하여 Store 정보를 최신 상태로 갱신.

- **Cache 만료 시 API 호출**:

    - Cache가 만료되었을 때만 오픈 API를 호출해 데이터를 가져옴.

- **데이터 응답 최적화**:

    - Redis에서 빠르게 데이터를 조회해 사용자 응답 시간 최소화.

### 3️⃣ 상품명 검색 기능

**설명**

- 상점에서 구매하려는 상품을 검색할 수 있습니다.

**세부 기능**

- **상품명 자동 완성**:

    - Page와 Pageable 객체를 사용해 검색 결과를 페이지 단위로 제공.

    - **LIKE 쿼리**를 사용해 입력된 검색어와 유사한 상품명을 실시간으로 추천.

    - Redis에 자주 검색된 상품명을 캐싱해 성능 최적화.

    - 비동기 API 호출로 검색 시 실시간 추천 제공.

- **필터 기능**:

    - 가격, 재화 종류, 레벨 제한 조건으로 검색 필터링.

    - 여러 조건을 조합한 동적 쿼리 지원.

- **정렬 옵션**:

    - 한글 가나다순, 영어 ABCD순, 가격순 정렬 제공.

    - Pageable 객체를 사용해 정렬과 페이징을 동시에 처리.

### 4️⃣ 인게임 재화 및 포인트로 구매 기능

**설명**

- 게임에서 획득한 인게임 재화(골드)와 포인트를 활용해 이커머스 상점에서 아이템을 구매합니다.

**세부 기능**

- **골드 결제**:

    - 유저의 잔여 골드를 확인하고 결제 처리.

    - **골드 부족 시** 결제 실패 메시지를 전달.

- **구매 확정 시 인벤토리 반영**:

    - 결제 완료 후 유저의 인벤토리에 상품 정보 반영.

    - **상품 재고 부족 시** 결제 실패 처리 및 알림 표시.

    - 재고 관리 및 동시성 문제 해결을 위한 **락(Lock) 처리**.

- **환불 기능**:

    - 구매 취소 시 DB에 저장되어 있는 구매 이력을 확인하여 구매 취소를 처리.

    - JPA를 통해 환불 이력 관리.

- **결제 시 포인트 적립**:

    - Spring Event Listener를 사용해 결제 성공 시 포인트 적립 이벤트 트리거.

### 5️⃣ 장바구니 기능

**설명**

- 유저가 상품을 장바구니에 담아 관리하고, 나중에 결제할 수 있습니다.

**세부 기능**

- **상품 담기**:

    - 동일 상품 선택 시 수량 자동 증가.

- **수량 변경 및 삭제**:

    - Redis에서 상품 수량 조정 및 삭제 처리.

    - 수량이 0이 되면 장바구니에서 삭제.

- **장바구니 조회**:

    - Redis 캐시에서 장바구니 데이터를 불러와 표시.

    - 장바구니 총 가격과 결제에 필요한 재화를 계산해 제공.

- **장바구니 결제**:

    - 장바구니 결제 시 유저의 구매 취소를 대비해 DB에 구매 이력을 저장.

### 6️⃣ 포인트 적립 기능

**설명**

- 유저가 결제한 금액에 따라 포인트를 적립해 추가 혜택을 제공합니다.

**세부 기능**

- **일반 포인트 적립**:

    - 결제 금액의 3%를 포인트로 적립.

    - Spring Event Listener로 포인트 적립 트리거 발생.

- **특정 레벨 이상 시 차등 적립**:

    - 특정 레벨 이상은 최대 10%까지 차등 적립.

    - 로스트아크 오픈 API로 특정 레벨 조건 확인.

- **포인트 유효 기간**:

    - 6개월 내 사용 제한.

- **포인트 회수**:

    - **구매 취소 시** 적립된 포인트는 회수. 단, 이미 사용한 포인트는 차감하지 않음.

### 7️⃣ 레벨별 접근 제한 상품 제공

**설명**

- 캐릭터 레벨에 따라 상점에서 구매 가능한 상품을 제어합니다.

**세부 기능**

- **레벨별 한정 상품**:

    - 캐릭터 레벨 정보는 로스트아크 오픈 API로 실시간 조회.

    - Redis에 일정 시간 캐시해 반복 조회 부하 감소.

- **레벨 업 보상**:

    - 레벨 달성 시 자동으로 할인 쿠폰 발급.

    - **쿠폰 발급 기준**: 특정 레벨에 도달할 때마다(예: 레벨 50, 100, 150) 발급.

    - **할인율**: 기본 10%, 특정 레벨 이상 시(예: 150 레벨) 20% 할인.

    - **만료기간**: 발급 후 30일 내 사용 가능.

    - **사용 조건**: 최소 구매 금액 1000 골드 이상 구매 시 적용.

    - 정해진 기한이 지나면 쿠폰 자동 삭제.

## 🧑‍💻 기술 스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.3.4
- **Build Tool**: Gradle
- **Database**:
    - **Production**: MariaDB
    - **Development**: H2 Database
- **ORM**: JPA (Java Persistence API)
- **Library**: Lombok, Hibernate Validator
- **Security**: Spring Security, JWT (JSON Web Token)
- **Cache**: Redis (Store 정보 캐시 및 장바구니 세션 유지)
- **Batch Processing**: Spring Scheduler (Batch 작업으로 Store 정보 주기적 갱신)
- **Event Handling**: Spring Event Listener (포인트 적립 및 기타 이벤트 처리)
- **Test**: Junit5
- **Server**: Embedded Tomcat
- **Open API**: LostArk Open API (캐릭터, 거래소 정보 연동)
- **IDE**: Eclipse

## 📂 프로젝트 구조  
1. **회원 정보 관리**: 유저의 로스트아크 계정과 프로필 관리  
3. **Store 정보 실시간 갱신**: Redis Cache와 Batch 작업을 조합해 데이터 실시간 반영  
4. **인게임 재화 및 포인트로 구매 기능**: 재화 결제 및 인벤토리 반영  
5. **장바구니 기능**: 상품 추가, 조회 및 결제 처리  
6. **포인트 적립 및 보상**: 결제 금액에 따른 포인트 제공  
7. **레벨 제한 상품 관리**: 특정 레벨 이상 접근 가능한 상품 제공  
8. **상품 검색 기능**: 필터와 정렬을 통한 상품 탐색  

## 💾 ERD
<img src="https://github.com/user-attachments/assets/e8a218a8-ddf1-4260-9179-a0720f278d0f" width="800" height="550"/>  

## 🔗 API 연동 정보  
- **LostArk Open API**를 활용해 **캐릭터 정보**와 **거래소 정보**를 불러옵니다.  
