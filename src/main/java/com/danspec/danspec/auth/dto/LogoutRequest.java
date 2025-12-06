package com.danspec.danspec.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogoutRequest {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[\\w.-]+@dankook\\.ac\\.kr$", message = "단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.")
    private String email;
}












