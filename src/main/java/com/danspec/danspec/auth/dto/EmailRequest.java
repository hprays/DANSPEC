package com.danspec.danspec.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @Pattern(regexp = "^[\\w.-]+@dankook\\.ac\\.kr$", message = "단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.")
    private String email;
}
