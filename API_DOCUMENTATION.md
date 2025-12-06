# 단스펙 API 문서

## 인증 API

### 1. 이메일 인증 요청
**POST** `/api/auth/email/request`

**Request Body:**
```json
{
  "email": "example@dankook.ac.kr"
}
```

**Response:**
```json
{
  "success": true,
  "message": "인증코드를 발송했습니다.",
  "data": null
}
```

**에러:**
- `400`: 단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.
- `400`: 이미 가입된 이메일입니다.

---

### 2. 이메일 인증 확인
**POST** `/api/auth/email/verify`

**Request Body:**
```json
{
  "email": "example@dankook.ac.kr",
  "code": "123456"
}
```

**Response:**
```json
{
  "success": true,
  "message": "이메일 인증이 완료되었습니다.",
  "data": null
}
```

**에러:**
- `400`: 인증 정보를 찾을 수 없습니다.
- `400`: 인증번호가 만료되었습니다.
- `400`: 인증번호가 올바르지 않습니다.

---

### 3. 회원가입
**POST** `/api/auth/signup`

**Request Body:**
```json
{
  "email": "example@dankook.ac.kr",
  "password": "password1234",
  "passwordConfirm": "password1234",
  "name": "홍길동",
  "campus": "JUKJEON",
  "college": "SW융합대학",
  "major": "통계데이터사이언스학과",
  "grade": "2",
  "interestJobPrimary": "데이터 분석가",
  "interestJobSecondary": "마케터",
  "interestJobTertiary": "기획자",
  "tagline": "나를 잘 보여줄 수 있는 소개 글"
}
```

**Response:**
```json
{
  "success": true,
  "message": "회원가입이 완료되었습니다.",
  "data": null
}
```

**에러:**
- `400`: 단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.
- `400`: 비밀번호가 일치하지 않습니다.
- `400`: 비밀번호는 10자 이상 20자 이하여야 합니다.
- `400`: 이미 가입된 이메일입니다.
- `400`: 이메일 인증이 완료되지 않았습니다.

---

### 4. 로그인
**POST** `/api/auth/login`

**Request Body:**
```json
{
  "email": "example@dankook.ac.kr",
  "password": "password1234"
}
```

**Response:**
```json
{
  "success": true,
  "message": "성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000
  }
}
```

**에러:**
- `400`: 단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.
- `401`: 이메일 또는 비밀번호가 올바르지 않습니다.

---

### 5. 토큰 갱신
**POST** `/api/auth/token/refresh`

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9..."
}
```

**Response:**
```json
{
  "success": true,
  "message": "성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000
  }
}
```

---

## 사용자 API (인증 필요)

### 6. 내 정보 조회
**GET** `/api/users/me`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:**
```json
{
  "success": true,
  "message": "성공",
  "data": {
    "id": 1,
    "email": "example@dankook.ac.kr",
    "name": "홍길동",
    "campus": "JUKJEON",
    "college": "SW융합대학",
    "major": "통계데이터사이언스학과",
    "grade": "2",
    "interestJobPrimary": "데이터 분석가",
    "interestJobSecondary": "마케터",
    "interestJobTertiary": "기획자",
    "tagline": "나를 잘 보여줄 수 있는 소개 글",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
}
```

---

### 7. 내 정보 수정
**PUT** `/api/users/me`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "name": "홍길동",
  "campus": "JUKJEON",
  "college": "SW융합대학",
  "major": "통계데이터사이언스학과",
  "grade": "3",
  "interestJobPrimary": "데이터 분석가",
  "interestJobSecondary": "마케터",
  "interestJobTertiary": "기획자",
  "tagline": "업데이트된 소개 글"
}
```

**Response:**
```json
{
  "success": true,
  "message": "회원정보가 수정되었습니다.",
  "data": {
    "id": 1,
    "email": "example@dankook.ac.kr",
    "name": "홍길동",
    "campus": "JUKJEON",
    "college": "SW융합대학",
    "major": "통계데이터사이언스학과",
    "grade": "3",
    "interestJobPrimary": "데이터 분석가",
    "interestJobSecondary": "마케터",
    "interestJobTertiary": "기획자",
    "tagline": "업데이트된 소개 글",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-02T00:00:00"
  }
}
```

---

## 에러 응답 형식

```json
{
  "success": false,
  "message": "에러 메시지",
  "data": null
}
```

## Campus Enum 값
- `JUKJEON`: 죽전
- `CHEONAN`: 천안

