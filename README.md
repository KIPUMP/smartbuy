# SmartBuy: AI 기반 최저가 쇼핑 도우미

사용자의 자연어 요청을 이해하고, 적절한 쇼핑 검색어로 변환한 뒤 외부 쇼핑 API를 호출하여 최저가 상품 정보를 제공하는 AI 기반 쇼핑 서비스입니다.

예를 들어 사용자가 `"내가 지금 필요한 물건 중 가장 싼 건 뭐야?"`, `"자취생이 쓰기 좋은 가성비 전자레인지 찾아줘"` 와 같이 입력하면, SmartBuy는 해당 문장을 분석하여 상품 카테고리와 의도를 파악하고, 실제 쇼핑 검색에 적합한 질의로 정제한 뒤 최저가 상품 목록을 제공합니다.

---

## 1. 프로젝트 개요

- **프로젝트명**: SmartBuy
- **한줄 소개**: 자연어 기반 상품 검색과 최저가 탐색을 지원하는 AI 쇼핑 도우미
- **목표**:
  - 사용자의 모호한 자연어 요청을 AI가 이해할 수 있도록 처리
  - 쇼핑 API와 연동하여 실제 상품 데이터를 조회
  - 가격, 판매처, 상품명 등을 기준으로 최적의 구매 선택지를 제공
  - 추후 검색 이력, 관심 상품, 개인화 추천 기능으로 확장 가능하도록 구조 설계

---

## 2. 주요 문제 정의

기존 쇼핑 서비스는 사용자가 정확한 상품명을 알고 있어야 원하는 검색 결과를 얻기 쉽습니다. 하지만 실제 사용자는 아래와 같이 검색하는 경우가 많습니다.

- "가성비 좋은 무선 이어폰 추천해줘"
- "학생이 들고 다니기 좋은 가벼운 노트북"
- "지금 가장 싼 생필품 뭐가 있을까"

이처럼 자연어 기반 요청은 단순 키워드 검색만으로 처리하기 어렵습니다. SmartBuy는 이 문제를 해결하기 위해 **AI 기반 질의 정제(Query Refinement)** 와 **외부 쇼핑 API 연동**을 결합한 구조를 채택합니다.

---

## 3. 프로젝트 아키텍처

## 3-1. 전체 구성

```text
[ Client (Web / Browser) ]
          |
          v
[ Spring Boot Server ]
   ├─ Controller Layer
   ├─ Search Service Layer
   ├─ AI Service Layer
   ├─ External Shopping Integration Layer
   ├─ Persistence Layer (JPA)
   └─ Cache Layer (Redis)
          |
          +--> [ OpenAI / LLM ]
          |
          +--> [ Naver Shopping API ]
          |
          +--> [ PostgreSQL ]
          |
          +--> [ Redis ]
```

---

## 3-2. 처리 흐름

1. 사용자가 웹 화면에서 자연어로 상품 검색 요청을 입력합니다.
2. Controller가 HTTP 요청을 수신합니다.
3. SearchService가 요청을 받아 전체 검색 흐름을 제어합니다.
4. AiSearchService가 사용자 문장을 분석하여 검색용 질의로 정제합니다.
5. Shopping Client가 외부 쇼핑 API를 호출합니다.
6. 응답 데이터를 서비스 내부 형식으로 변환합니다.
7. 필요 시 검색 이력을 DB에 저장하고 Redis에 캐싱합니다.
8. 가공된 상품 결과를 사용자에게 반환합니다.

---

## 3-3. 패키지 구조

```text
com.smartbuy
├── SmartBuyApplication.java
│
├── global
│   ├── config
│   ├── exception
│   ├── handler
│   ├── response
│   └── util
│
├── web
│   └── controller
│       ├── HomeController.java
│       ├── SearchController.java
│       └── ProductController.java
│
├── domain
│   ├── product
│   │   ├── entity
│   │   ├── repository
│   │   ├── service
│   │   └── dto
│   │
│   ├── search
│   │   ├── service
│   │   └── dto
│   │
│   ├── history
│   │   ├── entity
│   │   ├── repository
│   │   ├── service
│   │   └── dto
│   │
│   └── user
│       ├── entity
│       ├── repository
│       ├── service
│       └── dto
│
├── ai
│   ├── service
│   ├── prompt
│   ├── parser
│   └── dto
│
├── integration
│   ├── shopping
│   │   └── naver
│   │       ├── client
│   │       ├── dto
│   │       └── mapper
│   │
│   └── ai
│       ├── client
│       └── dto
│
└── infra
    ├── redis
    ├── security
    └── persistence
```

---

## 3-4. 패키지 설계 의도

### global
프로젝트 전반에서 공통으로 사용하는 설정, 예외, 응답 형식을 관리합니다.

### web.controller
사용자의 HTTP 요청을 받아 서비스 계층으로 전달하는 진입점입니다.

### domain
서비스의 핵심 비즈니스 영역입니다.
- `user`: 사용자 정보 관리
- `product`: 상품 정보 관리
- `search`: 검색 기능의 핵심 흐름 담당
- `history`: 검색 이력 저장 및 조회

### ai
자연어 처리와 검색어 정제 로직을 담당합니다.
- 프롬프트 관리
- LLM 응답 파싱
- 검색 질의 생성

### integration
외부 시스템과의 연동을 담당합니다.
- 네이버 쇼핑 API
- 추후 쿠팡, 11번가, G마켓 등 추가 가능
- 외부 AI API 연동 분리 가능

### infra
Redis, Security, Persistence 등 기술 인프라 관련 설정을 관리합니다.

---

## 4. 사용 기술 스택

## 4-1. Backend

- **Java 17**
- **Spring Boot 3.5.13**
- **Spring Web**
- **Spring Data JPA**
- **Spring Validation**
- **Spring AI**
- **Lombok**

### 선정 이유
- Java는 안정성과 생산성이 높고, 백엔드 서비스 구조화에 적합합니다.
- Spring Boot는 빠른 초기 개발과 확장성 있는 구조 설계에 유리합니다.
- JPA는 도메인 중심 설계를 진행하기 좋고 CRUD 생산성이 높습니다.
- Spring AI는 LLM 연동을 Spring 생태계 안에서 일관성 있게 처리하는 데 유리합니다.

---

## 4-2. Database

- **PostgreSQL**
- **Redis**

### 선정 이유
- PostgreSQL은 안정적인 오픈소스 RDBMS이며 확장성과 표준 SQL 지원이 우수합니다.
- Redis는 검색 결과 캐싱, 인기 검색어, 임시 데이터 저장 등에 적합합니다.

---

## 4-3. AI / External API

- **OpenAI API** 또는 LLM API
- **Naver Shopping Search API**

### 선정 이유
- OpenAI 계열 LLM은 자연어 의도 파악, 검색어 정제, 추천 문장 생성에 적합합니다.
- Naver Shopping API는 국내 쇼핑 데이터 기반 상품 검색에 활용하기 좋습니다.

---

## 4-4. Infra / DevOps

- **Docker**
- **Docker Compose**
- **Gradle**
- **GitHub**
- **VS Code / IntelliJ**
- **Codex CLI**

### 선정 이유
- Docker는 개발 환경과 배포 환경의 일관성을 확보하는 데 유리합니다.
- Gradle은 Spring Boot 프로젝트 빌드 속도와 관리 측면에서 효율적입니다.
- GitHub는 형상관리와 협업에 필수적입니다.
- Codex CLI는 개발 생산성을 높이고 반복 코드 작성에 도움을 줄 수 있습니다.

---

## 4-5. Frontend

초기 MVP 기준에서는 Spring Boot 기반 서버 렌더링 또는 간단한 웹 UI를 우선 적용하고, 이후 다음과 같이 확장 가능합니다.

- HTML / CSS / JavaScript
- Thymeleaf 또는 React / Next.js 확장 가능

---

## 5. 핵심 기능 명세서

## 5-1. 자연어 상품 검색

### 설명
사용자가 일반적인 검색 키워드가 아닌 자연어 문장으로 요청하더라도 적절한 상품 검색 결과를 제공합니다.

### 입력 예시
- "가성비 좋은 무선 이어폰 추천해줘"
- "자취방에서 쓸 작은 전자레인지"
- "지금 당장 가장 싸게 살 수 있는 생필품"

### 처리 내용
- 사용자 입력 수신
- 핵심 카테고리 및 조건 추출
- 검색용 질의 생성
- 외부 쇼핑 API 호출
- 검색 결과 반환

### 기대 효과
- 사용자가 정확한 상품명을 몰라도 검색 가능
- 검색 편의성 향상

---

## 5-2. AI 기반 검색어 정제

### 설명
자연어 기반 요청을 실제 쇼핑 검색에 적합한 짧고 명확한 질의로 변환합니다.

### 예시
- 원본: "학생이 쓰기 좋은 가벼운 노트북"
- 정제: "가벼운 노트북"

- 원본: "자취생이 쓰기 좋은 전기포트 추천"
- 정제: "전기포트"

### 처리 내용
- 프롬프트 생성
- LLM 호출
- 응답 파싱
- 정제 검색어 생성

### 기대 효과
- 검색 품질 향상
- 불필요한 키워드 제거

---

## 5-3. 최저가 상품 조회

### 설명
외부 쇼핑 API 응답을 기준으로 상품 가격 정보를 정렬하여 사용자에게 더 저렴한 상품을 우선적으로 보여줍니다.

### 처리 내용
- 상품 목록 수신
- 가격 기준 정렬
- 판매처, 상품명, 링크, 이미지 제공

### 기대 효과
- 사용자의 가격 비교 시간 절약

---

## 5-4. 검색 이력 저장

### 설명
사용자의 검색 원문과 AI 정제 검색어를 저장하여 이후 추천, 통계, 재검색 기능으로 활용합니다.

### 저장 항목 예시
- 사용자 ID
- 원본 검색어
- 정제 검색어
- 검색 시각

### 기대 효과
- 사용자 행동 분석 가능
- 개인화 기능 확장 가능

---

## 5-5. 상품 상세 조회

### 설명
사용자가 선택한 상품의 상세 정보를 확인할 수 있도록 제공합니다.

### 제공 정보 예시
- 상품명
- 가격
- 판매처
- 상품 링크
- 이미지

---

## 5-6. 캐시 기반 검색 성능 개선

### 설명
동일하거나 유사한 검색어에 대해 Redis 캐시를 적용하여 응답 속도를 높입니다.

### 기대 효과
- 외부 API 호출 횟수 감소
- 응답 성능 개선
- 비용 절감

---

## 5-7. 향후 확장 기능

- 관심 상품 저장
- 사용자별 맞춤 추천
- 인기 검색어 제공
- 카테고리별 랭킹
- 다중 쇼핑몰 API 통합 검색
- 가격 변동 추적
- 알림 기능

---

## 6. 기능 우선순위

## MVP 1차
- 서버 실행 및 기본 구조 구성
- 자연어 검색 API
- AI 기반 검색어 정제
- 네이버 쇼핑 API 연동
- 검색 결과 응답

## MVP 2차
- 검색 이력 저장
- 상품 상세 조회
- 예외 처리 및 응답 통일
- Redis 캐시 도입

## 고도화 3차
- 개인화 추천
- 다중 쇼핑몰 비교
- 가격 추적
- 로그인 / 인증 기능

---

## 7. 주요 API 예시

## 7-1. 홈 상태 확인

### Request
```http
GET /
```

### Response
```json
{
  "success": true,
  "data": "SmartBuy 서버가 정상 실행 중입니다.",
  "error": null
}
```

---

## 7-2. 상품 검색

### Request
```http
POST /api/search
Content-Type: application/json
```

```json
{
  "query": "가성비 좋은 무선 이어폰 추천해줘"
}
```

### Response
```json
{
  "success": true,
  "data": {
    "originalQuery": "가성비 좋은 무선 이어폰 추천해줘",
    "refinedQuery": "무선 이어폰",
    "products": [
      {
        "title": "블루투스 무선 이어폰 A",
        "price": 29900,
        "mallName": "스마트스토어",
        "imageUrl": "https://...",
        "productUrl": "https://..."
      }
    ]
  },
  "error": null
}
```

---

## 8. DB 설계 방향

초기 핵심 테이블은 아래와 같습니다.

### users
사용자 기본 정보 저장

### products
상품 기본 정보 저장

### search_histories
사용자의 검색 원문, 정제 검색어, 검색 시간을 저장

추후 필요 시 아래 테이블을 확장할 수 있습니다.
- favorite_products
- search_cache
- recommendation_logs

---

## 9. 개발 원칙

- **관심사 분리**: Controller, Service, Repository, Client 역할 분리
- **확장 가능 구조**: 쇼핑 API 추가 시 integration 계층 확장
- **유지보수성**: 공통 응답/예외 처리 통일
- **도메인 중심 설계**: user, product, history, search 분리
- **AI 독립성 확보**: AI 호출 로직을 별도 계층으로 분리하여 교체 가능하도록 설계

---

## 10. 기대 효과

- 사용자는 정확한 상품명을 몰라도 자연어로 상품을 탐색할 수 있습니다.
- AI 기반 질의 정제를 통해 검색 효율을 높일 수 있습니다.
- 최저가 중심 상품 탐색으로 구매 의사결정을 돕습니다.
- 향후 개인화 추천 및 다중 쇼핑몰 통합 서비스로 확장 가능합니다.

---

## 11. 실행 계획 예시

1. 프로젝트 초기 세팅
2. 공통 응답 및 예외 구조 작성
3. Entity 및 Repository 작성
4. Search API 기본 기능 구현
5. Naver Shopping API 연동
6. AI 검색어 정제 기능 연결
7. 검색 이력 저장
8. Redis 캐싱 추가
9. UI 개선 및 배포

---

## 12. 실행 환경 예시

```yaml
Java: 17
Spring Boot: 3.5.13
Database: PostgreSQL
Cache: Redis
Build Tool: Gradle
Container: Docker
```

---
