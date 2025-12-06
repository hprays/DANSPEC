# 단스펙 API 테스트 명령어 (PowerShell)

## 기본 설정
PowerShell에서 아래 명령어들을 그대로 복사해서 사용하세요.

---

## 1. 이메일 인증 요청

```powershell
$body = '{"email":"hprays@dankook.ac.kr"}'
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/email/request" -Method POST -ContentType "application/json" -Body $body
```

**응답 확인:**
- `StatusCode : 200` → 성공
- `Content` 안에 `"success":true` 확인
- 실제 이메일함에서 인증코드 확인

---

## 2. 이메일 인증 확인

```powershell
$body = '{"email":"hprays@dankook.ac.kr","code":"123456"}'
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/email/verify" -Method POST -ContentType "application/json" -Body $body
```

**주의:** `"code"` 부분을 실제로 받은 6자리 인증코드로 바꿔주세요.

---

## 3. 회원가입

```powershell
$body = '{
  "email": "hprays@dankook.ac.kr",
  "password": "Password1234",
  "passwordConfirm": "Password1234",
  "name": "지은",
  "campus": "JUKJEON",
  "college": "SW융합대학",
  "major": "통계데이터사이언스학과",
  "grade": "2",
  "interestJobPrimary": "데이터 분석가",
  "interestJobSecondary": "마케터",
  "interestJobTertiary": "기획자",
  "tagline": "공모전 데이터 분석을 좋아하는 지은입니다."
}'

Invoke-WebRequest -Uri "http://localhost:8080/api/auth/signup" -Method POST -ContentType "application/json" -Body $body
```

---

## 4. 로그인 (토큰 받기)

```powershell
$body = '{
  "email": "hprays@dankook.ac.kr",
  "password": "Password1234"
}'

$response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body $body
$json = $response.Content | ConvertFrom-Json
$accessToken = $json.data.accessToken
$refreshToken = $json.data.refreshToken

# 토큰 확인
Write-Host "Access Token: $accessToken"
Write-Host "Refresh Token: $refreshToken"
```

**중요:** 이 명령어 실행 후 `$accessToken` 변수에 토큰이 저장됩니다.  
다음 명령어들에서 이 변수를 사용하세요.

---

## 5. 마이페이지 조회 (인증 필요)

```powershell
# 위에서 받은 $accessToken 사용
Invoke-WebRequest -Uri "http://localhost:8080/api/users/me" -Method GET -Headers @{ "Authorization" = "Bearer $accessToken" }
```

---

## 6. 마이페이지 수정 (인증 필요)

```powershell
$body = '{
  "name": "지은",
  "campus": "JUKJEON",
  "college": "SW융합대학",
  "major": "통계데이터사이언스학과",
  "grade": "3",
  "interestJobPrimary": "데이터 분석가",
  "interestJobSecondary": "마케터",
  "interestJobTertiary": "기획자",
  "tagline": "업데이트된 소개 글입니다."
}'

Invoke-WebRequest -Uri "http://localhost:8080/api/users/me" -Method PUT -ContentType "application/json" -Headers @{ "Authorization" = "Bearer $accessToken" } -Body $body
```

---

## 7. 비밀번호 변경

```powershell
$body = '{
  "email": "hprays@dankook.ac.kr",
  "currentPassword": "Password1234",
  "newPassword": "NewPassword1234",
  "newPasswordConfirm": "NewPassword1234"
}'

Invoke-WebRequest -Uri "http://localhost:8080/api/auth/password/change" -Method POST -ContentType "application/json" -Body $body
```

---

## 8. 로그아웃

```powershell
$body = '{"email":"hprays@dankook.ac.kr"}'
Invoke-WebRequest -Uri "http://localhost:8080/api/auth/logout" -Method POST -ContentType "application/json" -Body $body
```

---

## 전체 플로우 테스트 (한 번에 실행)

```powershell
# 1. 이메일 인증 요청
$body1 = '{"email":"hprays@dankook.ac.kr"}'
$res1 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/email/request" -Method POST -ContentType "application/json" -Body $body1
Write-Host "1. 이메일 인증 요청: $($res1.StatusCode)"

# 2. 이메일 인증 확인 (인증코드는 실제로 받은 것으로 바꿔주세요)
$body2 = '{"email":"hprays@dankook.ac.kr","code":"123456"}'
$res2 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/email/verify" -Method POST -ContentType "application/json" -Body $body2
Write-Host "2. 이메일 인증 확인: $($res2.StatusCode)"

# 3. 회원가입
$body3 = '{
  "email": "hprays@dankook.ac.kr",
  "password": "Password1234",
  "passwordConfirm": "Password1234",
  "name": "지은",
  "campus": "JUKJEON",
  "college": "SW융합대학",
  "major": "통계데이터사이언스학과",
  "grade": "2",
  "interestJobPrimary": "데이터 분석가",
  "interestJobSecondary": "마케터",
  "interestJobTertiary": "기획자",
  "tagline": "공모전 데이터 분석을 좋아하는 지은입니다."
}'
$res3 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/signup" -Method POST -ContentType "application/json" -Body $body3
Write-Host "3. 회원가입: $($res3.StatusCode)"

# 4. 로그인
$body4 = '{"email":"hprays@dankook.ac.kr","password":"Password1234"}'
$res4 = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body $body4
$json = $res4.Content | ConvertFrom-Json
$accessToken = $json.data.accessToken
Write-Host "4. 로그인: $($res4.StatusCode), Token: $($accessToken.Substring(0,20))..."

# 5. 마이페이지 조회
$res5 = Invoke-WebRequest -Uri "http://localhost:8080/api/users/me" -Method GET -Headers @{ "Authorization" = "Bearer $accessToken" }
Write-Host "5. 마이페이지 조회: $($res5.StatusCode)"

Write-Host "`n전체 테스트 완료!"
```

---

## 응답 확인 팁

- **성공:** `StatusCode : 200` + `Content` 안에 `"success":true`
- **에러:** `StatusCode : 400/401/500` → `Content` 안에 에러 메시지 확인
- **한글 깨짐:** PowerShell 인코딩 문제일 수 있지만, 실제 JSON 값은 정상입니다.

---

## 주의사항

1. **서버가 실행 중이어야 함** (`DanspecApplication` Run 상태)
2. **이메일 주소**는 실제 단국대 이메일로 변경
3. **인증코드**는 실제로 받은 6자리 숫자로 변경
4. **토큰**은 로그인 후 `$accessToken` 변수에 저장되어 있어야 마이페이지 API 사용 가능

