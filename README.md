# 사장EZ 백엔드 API

소상공인을 위한 매출 관리 플랫폼의 백엔드 REST API 서버입니다.

## 🚀 기술 스택

- **Java 21**: 최신 LTS 버전
- **Spring Boot 3.5.4**: 웹 애플리케이션 프레임워크
- **Spring Data JPA**: 데이터 액세스 레이어
- **PostgreSQL**: 메인 데이터베이스
- **Lombok**: 코드 간소화
- **Gradle**: 빌드 도구

## 📦 주요 기능

- 매출 데이터 CRUD 관리
- 사용자별 매출 통계 제공
- 기간별 매출 조회
- RESTful API 엔드포인트
- 데이터 검증 및 예외 처리

## 🛠 개발 환경 설정

### 필수 요구사항

- Java 21 이상
- PostgreSQL 13 이상
- Gradle 8.0 이상

### 데이터베이스 설정

1. PostgreSQL 설치 및 실행
2. 데이터베이스 생성:
```sql
CREATE DATABASE sajangez_db;
CREATE USER sajangez_user WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE sajangez_db TO sajangez_user;
```

### 환경변수 설정

`.env` 파일을 생성하거나 시스템 환경변수 설정:
```bash
DB_USERNAME=sajangez_user
DB_PASSWORD=password
```

### 실행 방법

```bash
# 개발 모드 실행
./gradlew bootRun

# 빌드
./gradlew build

# JAR 파일 실행
java -jar app/build/libs/app-0.0.1-SNAPSHOT.jar
```

## 📋 API 엔드포인트

### 매출 관리

- `POST /api/sales` - 매출 데이터 생성/업데이트
- `GET /api/sales/user/{userId}` - 사용자별 전체 매출 조회
- `GET /api/sales/user/{userId}/range` - 기간별 매출 조회
- `GET /api/sales/user/{userId}/date/{date}` - 특정 날짜 매출 조회
- `GET /api/sales/user/{userId}/statistics` - 매출 통계 조회
- `DELETE /api/sales/{saleId}/user/{userId}` - 매출 데이터 삭제

### 헬스체크

- `GET /actuator/health` - 서버 상태 확인
- `GET /actuator/info` - 애플리케이션 정보
- `GET /actuator/metrics` - 메트릭스 정보

## 📖 API 사용 예제

### 매출 데이터 생성
```bash
curl -X POST http://localhost:8080/api/sales \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "saleDate": "2024-01-15",
    "amount": 150000,
    "storeName": "홍길동 치킨집",
    "businessType": "치킨전문점"
  }'
```

### 사용자 매출 조회
```bash
curl http://localhost:8080/api/sales/user/user123
```

### 매출 통계 조회
```bash
curl http://localhost:8080/api/sales/user/user123/statistics
```

## 🗄️ 데이터베이스 스키마

### Sales 테이블
```sql
CREATE TABLE sales (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    sale_date DATE NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    store_name VARCHAR(255),
    business_type VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    UNIQUE(user_id, sale_date)
);
```

### Users 테이블
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    store_name VARCHAR(255) NOT NULL,
    business_type VARCHAR(255),
    address TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## 🔧 개발 도구

### 로그 확인
```bash
# 애플리케이션 로그
tail -f logs/application.log

# SQL 쿼리 로그는 콘솔에서 확인 가능 (show-sql: true)
```

### 데이터베이스 접속
```bash
psql -h localhost -U sajangez_user -d sajangez_db
```

## 📝 개발 가이드라인

1. **코드 스타일**: Google Java Style Guide 준수
2. **커밋 메시지**: [Conventional Commits](https://www.conventionalcommits.org/) 형식 사용
3. **API 문서**: 컨트롤러에 상세한 주석 작성
4. **테스트**: 주요 비즈니스 로직에 대한 단위 테스트 작성

## 🚨 문제 해결

### 자주 발생하는 오류

1. **데이터베이스 연결 실패**
   - PostgreSQL 서비스 실행 상태 확인
   - 데이터베이스 연결 정보 확인

2. **포트 충돌**
   - 8080 포트 사용 중인 프로세스 확인: `lsof -i :8080`

3. **의존성 문제**
   - Gradle 캐시 정리: `./gradlew clean`

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 제공됩니다.

---

**사장EZ** - 소상공인의 성공을 위한 기술 파트너 🚀