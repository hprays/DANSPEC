# 단스펙 (DANSPEC) 백엔드

대학생을 위한 AI 기반 자기계발/커리어 설계 앱의 백엔드 서버입니다.

## 🚀 기술 스택

- **Java 21**
- **Spring Boot 3.5.8**
- **Spring Security** - 인증 및 보안
- **JWT** - 토큰 기반 인증
- **Spring Data JPA** - 데이터베이스 ORM
- **MySQL** - 데이터베이스
- **Gradle** - 빌드 도구
- **Gmail SMTP** - 이메일 인증

## 📋 주요 기능

### 인증 시스템

- ✅ 단국대학교 이메일(@dankook.ac.kr) 인증
- ✅ 이메일 인증코드 발송 및 검증 (6자리, 5분 유효)
- ✅ 회원가입 (이메일 인증 필수, 비밀번호 10-20자 검증)
- ✅ JWT 기반 로그인 (Access Token + Refresh Token)
- ✅ 토큰 갱신
- ✅ 비밀번호 변경
- ✅ 로그아웃

### 사용자 관리

- ✅ 마이페이지 조회
- ✅ 마이페이지 정보 수정
- ✅ 사용자 프로필 관리 (캠퍼스, 전공, 학년, 희망 직무 등)

## 🛠️ 실행 방법

### 1. 사전 요구사항

- JDK 21 이상
- MySQL 8.0 이상
- Gradle (또는 Gradle Wrapper 사용)

### 2. 데이터베이스 설정

MySQL에서 데이터베이스 생성:

```sql
CREATE DATABASE danspec_db;
```

### 3. 설정 파일 수정

`src/main/resources/application.yaml` 파일에서 다음 정보를 수정하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/danspec_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: your_db_username
    password: your_db_password
  mail:
    username: your_gmail@gmail.com
    password: your_gmail_app_password # Gmail 앱 비밀번호

jwt:
  secret: your_jwt_secret_key
```

### 4. 실행

```bash
# Gradle Wrapper 사용
./gradlew bootRun

# 또는 IntelliJ에서 DanspecApplication 실행
```

서버가 `http://localhost:8080` 에서 실행됩니다.

## 📡 API 엔드포인트

### 인증 API

- `POST /api/auth/email/request` - 이메일 인증코드 발송
- `POST /api/auth/email/verify` - 이메일 인증코드 확인
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/token/refresh` - 토큰 갱신
- `POST /api/auth/password/change` - 비밀번호 변경
- `POST /api/auth/logout` - 로그아웃

### 사용자 API (인증 필요)

- `GET /api/users/me` - 내 정보 조회
- `PUT /api/users/me` - 내 정보 수정

자세한 API 문서는 [API_DOCUMENTATION.md](./API_DOCUMENTATION.md) 를 참고하세요.

## 🧪 API 테스트

PowerShell에서 API 테스트 명령어는 [API_TEST_COMMANDS.md](./API_TEST_COMMANDS.md) 를 참고하세요.

### 예시: 이메일 인증 요청

```powershell
$body = '{"email":"your_email@dankook.ac.kr"}'
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/email/request" -Method POST -ContentType "application/json" -Body $body
```

## 📁 프로젝트 구조

```
src/main/java/com/danspec/danspec/
├── auth/                    # 인증 관련 (로그인, 회원가입, JWT)
│   ├── controller/          # API 엔드포인트
│   ├── domain/              # 엔티티 (EmailVerification, RefreshToken)
│   ├── dto/                 # 요청/응답 객체
│   ├── jwt/                 # JWT 토큰 처리
│   ├── repository/          # 데이터베이스 접근
│   └── service/             # 비즈니스 로직
├── user/                    # 사용자 관련 (마이페이지)
│   ├── controller/
│   ├── domain/              # User 엔티티
│   ├── dto/
│   ├── repository/
│   └── service/
├── common/                  # 공통 (예외 처리, 응답 DTO)
├── config/                  # 설정 (Security, JWT)
├── mail/                    # 이메일 발송
└── DanspecApplication.java  # 메인 애플리케이션
```

자세한 파일 기능 설명은 [FILES_FUNCTIONALITY_GUIDE.md](./FILES_FUNCTIONALITY_GUIDE.md) 를 참고하세요.

## 🔒 보안

- 비밀번호는 BCrypt로 암호화되어 저장됩니다
- JWT 토큰 기반 인증 사용
- 단국대학교 이메일(@dankook.ac.kr)만 회원가입 가능
- CORS 설정으로 프론트엔드와 안전하게 연동

## 📊 데이터베이스 테이블

- `users` - 사용자 정보
- `email_verifications` - 이메일 인증 정보
- `refresh_tokens` - 리프레시 토큰

## ⚠️ 주의사항

1. **환경 변수 관리**: `application.yaml`에 민감한 정보(DB 비밀번호, Gmail 비밀번호, JWT 시크릿)가 포함되어 있습니다. 운영 환경에서는 환경 변수로 관리하세요.

2. **이메일 설정**: Gmail SMTP를 사용하려면 Gmail 계정에서 "앱 비밀번호"를 생성해야 합니다.

3. **데이터베이스**: `ddl-auto: update` 설정으로 테이블이 자동 생성됩니다. 운영 환경에서는 `validate` 또는 `none`으로 변경하세요.

## 📝 라이선스

이 프로젝트는 교육 목적으로 제작되었습니다.
