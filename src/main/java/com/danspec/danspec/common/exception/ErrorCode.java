package com.danspec.danspec.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 인증 관련
    EMAIL_NOT_VERIFIED("이메일 인증이 완료되지 않았습니다."),
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다."),
    EMAIL_NOT_FOUND("가입되지 않은 이메일입니다."),
    INVALID_CREDENTIALS("이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_EMAIL_DOMAIN("단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다."),
    VERIFICATION_CODE_EXPIRED("인증번호가 만료되었습니다."),
    VERIFICATION_CODE_INVALID("인증번호가 올바르지 않습니다."),
    VERIFICATION_NOT_FOUND("인증 정보를 찾을 수 없습니다."),

    // 토큰 관련
    TOKEN_INVALID("유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED("만료된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND("리프레시 토큰을 찾을 수 없습니다."),

    // 일반
    INTERNAL_SERVER_ERROR("서버 에러가 발생했습니다."),
    BAD_REQUEST("잘못된 요청입니다.");

    private final String message;
}
