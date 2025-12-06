# DANSPEC 프로젝트 파일 기능 가이드

---

## 📁 프로젝트 구조

```
src/main/java/com/danspec/danspec/
├── auth/                    # 인증 관련 (로그인, 회원가입, JWT)
├── user/                    # 사용자 관련 (마이페이지)
├── common/                  # 공통 (예외 처리, 응답 DTO)
├── config/                  # 설정 (Security, JWT)
├── mail/                    # 이메일 발송
└── DanspecApplication.java  # 메인 애플리케이션
```

---

## 🔐 auth 패키지 (인증 관련)

### 📂 controller/

#### `AuthController.java`

**기능:** 인증 관련 API 엔드포인트 제공

- `POST /api/auth/email/request` - 이메일 인증코드 발송 요청
- `POST /api/auth/email/verify` - 이메일 인증코드 확인
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인 (JWT 토큰 발급)
- `POST /api/auth/token/refresh` - 토큰 갱신
- `POST /api/auth/password/change` - 비밀번호 변경
- `POST /api/auth/logout` - 로그아웃 (리프레시 토큰 삭제)

---

### 📂 domain/ (엔티티)

#### `EmailVerification.java`

**기능:** 이메일 인증 정보 저장 (DB 테이블: `email_verifications`)

- 필드: `email`, `code` (6자리), `expiresAt`, `verified`
- 용도: 회원가입 전 이메일 인증코드 임시 저장

#### `RefreshToken.java`

**기능:** 리프레시 토큰 저장 (DB 테이블: `refresh_tokens`)

- 필드: `token`, `email`, `expiresAt`, `createdAt`
- 용도: 로그인 후 발급된 리프레시 토큰 관리

---

### 📂 dto/ (요청/응답 객체)

#### `EmailRequest.java`

**기능:** 이메일 인증 요청 DTO

- 필드: `email` (단국대 이메일만 허용 검증)

#### `EmailVerifyRequest.java`

**기능:** 이메일 인증 확인 DTO

- 필드: `email`, `code` (6자리 인증코드)

#### `SignupRequest.java`

**기능:** 회원가입 요청 DTO

- 필드: `email`, `password`, `passwordConfirm`, `name`, `campus`, `college`, `major`, `grade`, `interestJobPrimary`, `interestJobSecondary`, `interestJobTertiary`, `tagline`
- 검증: 이메일 도메인, 비밀번호 길이(10-20자), 영문+숫자 조합

#### `LoginRequest.java`

**기능:** 로그인 요청 DTO

- 필드: `email`, `password`

#### `TokenResponse.java`

**기능:** 로그인/토큰 갱신 응답 DTO

- 필드: `accessToken`, `refreshToken`, `tokenType` ("Bearer"), `expiresIn`

#### `RefreshTokenRequest.java`

**기능:** 토큰 갱신 요청 DTO

- 필드: `refreshToken`

#### `ChangePasswordRequest.java`

**기능:** 비밀번호 변경 요청 DTO

- 필드: `email`, `currentPassword`, `newPassword`, `newPasswordConfirm`

#### `LogoutRequest.java`

**기능:** 로그아웃 요청 DTO

- 필드: `email`

---

### 📂 jwt/ (JWT 토큰 처리)

#### `JwtTokenProvider.java`

**기능:** JWT 토큰 생성/검증

- `createAccessToken(email)` - 액세스 토큰 생성 (24시간)
- `createRefreshToken(email)` - 리프레시 토큰 생성 (7일)
- `getEmailFromToken(token)` - 토큰에서 이메일 추출
- `validateToken(token)` - 토큰 유효성 검증

#### `JwtAuthenticationFilter.java`

**기능:** 요청마다 JWT 토큰 검증하는 필터

- `Authorization: Bearer {token}` 헤더에서 토큰 추출
- 토큰이 유효하면 SecurityContext에 인증 정보 저장

---

### 📂 repository/ (데이터베이스 접근)

#### `EmailVerificationRepository.java`

**기능:** 이메일 인증 정보 DB 접근

- `findByEmail(email)` - 이메일로 인증 정보 조회

#### `RefreshTokenRepository.java`

**기능:** 리프레시 토큰 DB 접근

- `findByToken(token)` - 토큰으로 조회
- `findByEmail(email)` - 이메일로 조회
- `deleteByEmail(email)` - 이메일로 토큰 삭제 (로그아웃용)

---

### 📂 service/ (비즈니스 로직)

#### `AuthService.java`

**기능:** 인증 관련 핵심 비즈니스 로직

- `requestEmailVerification()` - 인증코드 생성 및 이메일 발송
- `verifyEmail()` - 인증코드 검증
- `signup()` - 회원가입 처리 (이메일 인증 확인, 비밀번호 암호화, User 저장)
- `login()` - 로그인 처리 (인증 후 JWT 토큰 발급)
- `refreshToken()` - 토큰 갱신
- `changePassword()` - 비밀번호 변경 (현재 비밀번호 확인 후 변경, 리프레시 토큰 삭제)
- `logout()` - 로그아웃 (리프레시 토큰 삭제)

#### `CustomUserDetailsService.java`

**기능:** Spring Security용 사용자 정보 로드

- `loadUserByUsername(email)` - 이메일로 User 조회하여 UserDetails 반환

---

## 👤 user 패키지 (사용자 관련)

### 📂 controller/

#### `UserController.java`

**기능:** 사용자 정보 API 엔드포인트

- `GET /api/users/me` - 내 정보 조회 (인증 필요)
- `PUT /api/users/me` - 내 정보 수정 (인증 필요)

---

### 📂 domain/ (엔티티)

#### `User.java`

**기능:** 사용자 정보 저장 (DB 테이블: `users`)

- 필드: `id`, `email`, `password` (암호화), `name`, `campus`, `college`, `major`, `grade`, `interestJobPrimary`, `interestJobSecondary`, `interestJobTertiary`, `tagline`, `createdAt`, `updatedAt`
- `@CreatedDate`, `@LastModifiedDate` - 자동 날짜 관리
- `update()` - 정보 수정 메서드

#### `type/Campus.java`

**기능:** 캠퍼스 Enum

- 값: `JUKJEON` (죽전), `CHEONAN` (천안)

---

### 📂 dto/

#### `UserResponse.java`

**기능:** 사용자 정보 응답 DTO

- User 엔티티를 JSON으로 변환할 때 사용

#### `UserUpdateRequest.java`

**기능:** 사용자 정보 수정 요청 DTO

- 필드: `name`, `campus`, `college`, `major`, `grade`, `interestJobPrimary`, `interestJobSecondary`, `interestJobTertiary`, `tagline`

---

### 📂 repository/

#### `UserRepository.java`

**기능:** 사용자 정보 DB 접근

- `findByEmail(email)` - 이메일로 사용자 조회
- `existsByEmail(email)` - 이메일 중복 확인

---

### 📂 service/

#### `UserService.java`

**기능:** 사용자 정보 비즈니스 로직

- `getMyProfile(email)` - 내 정보 조회
- `updateMyProfile(email, request)` - 내 정보 수정

---

## 🔧 common 패키지 (공통)

### 📂 dto/

#### `ApiResponse.java`

**기능:** 모든 API 응답의 공통 형식

- 필드: `success` (boolean), `message` (String), `data` (제네릭)
- `success(data)` - 성공 응답 생성
- `error(message)` - 에러 응답 생성

---

### 📂 exception/

#### `DanspecException.java`

**기능:** 커스텀 예외 클래스

- `ErrorCode`와 메시지를 함께 전달

#### `ErrorCode.java`

**기능:** 에러 코드 Enum

- 예: `EMAIL_ALREADY_EXISTS`, `INVALID_CREDENTIALS`, `TOKEN_EXPIRED` 등

#### `GlobalExceptionHandler.java`

**기능:** 전역 예외 처리

- `@RestControllerAdvice` - 모든 컨트롤러의 예외를 여기서 처리
- `DanspecException`, `MethodArgumentNotValidException`, `AuthenticationException` 등 처리
- 일관된 에러 응답 형식 반환

---

## ⚙️ config 패키지 (설정)

#### `SecurityConfig.java`

**기능:** Spring Security 설정

- CORS 설정 (프론트엔드 연동)
- JWT 필터 등록
- `/api/auth/**` 는 인증 불필요, 나머지는 인증 필요
- `PasswordEncoder` 빈 등록 (BCrypt)

#### `JwtProperties.java`

**기능:** JWT 설정값 관리

- `secret` - JWT 서명 키
- `expiration` - 액세스 토큰 만료 시간 (24시간)
- `refreshExpiration` - 리프레시 토큰 만료 시간 (7일)

---

## 📧 mail 패키지

### 📂 service/

#### `MailService.java`

**기능:** 이메일 발송

- `sendVerificationCode(to, code)` - 인증코드 이메일 발송 (Gmail SMTP 사용)

---

## 🚀 메인 애플리케이션

#### `DanspecApplication.java`

**기능:** Spring Boot 메인 클래스

- `@SpringBootApplication` - 애플리케이션 시작점
- `@EnableJpaAuditing` - JPA Auditing 활성화 (자동 날짜 관리)

---

## 📄 설정 파일

### `build.gradle`

**기능:** 프로젝트 의존성 관리

- **추가된 의존성:**
  - `spring-boot-starter-security` - Spring Security
  - `spring-boot-starter-mail` - 이메일 발송
  - `spring-boot-starter-validation` - 입력값 검증
  - `io.jsonwebtoken:jjwt-*` - JWT 라이브러리 (0.11.5 버전)

### `application.yaml`

**기능:** 애플리케이션 설정

- **데이터베이스:** MySQL 연결 정보
- **JPA:** `ddl-auto: update` (테이블 자동 생성)
- **메일:** Gmail SMTP 설정
- **JWT:** 시크릿 키, 만료 시간

---

## 📊 데이터베이스 테이블

### `users`

- 사용자 정보 저장
- 필드: `id`, `email`, `password`, `name`, `campus`, `college`, `major`, `grade`, `interest_job_primary`, `interest_job_secondary`, `interest_job_tertiary`, `tagline`, `created_at`, `updated_at`

### `email_verifications`

- 이메일 인증 정보 임시 저장
- 필드: `id`, `email`, `code`, `expires_at`, `verified`, `updated_at`

### `refresh_tokens`

- 리프레시 토큰 저장
- 필드: `id`, `token`, `email`, `expires_at`, `created_at`

---

## 🔄 전체 플로우

1. **회원가입:**

   - 이메일 인증 요청 → 인증코드 발송 → 인증코드 확인 → 회원가입

2. **로그인:**

   - 이메일/비밀번호 입력 → JWT 토큰 발급 (access + refresh)

3. **마이페이지:**

   - 토큰으로 인증 → 내 정보 조회/수정

4. **비밀번호 변경:**

   - 현재 비밀번호 확인 → 새 비밀번호로 변경 → 리프레시 토큰 삭제 (재로그인 필요)

5. **로그아웃:**
   - 리프레시 토큰 삭제

---

## ⚠️ 주의사항

1. **패키지명 변경:**

   - `com.danspec.danspec` → 레포의 패키지명으로 변경 (예: `com.parksehyn.danspec`)
   - 모든 파일의 맨 위 `package ...` 부분 수정

2. **의존성 확인:**

   - `build.gradle` 에 JWT, Security, Mail 라이브러리가 있는지 확인
   - 없으면 추가

3. **설정 파일:**

   - `application.yaml` 의 DB/메일 정보는 레포 환경에 맞게 수정
   - 민감 정보는 환경변수로 관리 권장

4. **테이블 생성:**
   - `ddl-auto: update` 설정이면 자동으로 테이블 생성됨
   - 또는  레포의 기존 테이블 구조와 맞는지 확인

---
