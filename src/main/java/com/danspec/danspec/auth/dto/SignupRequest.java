package com.danspec.danspec.auth.dto;

import com.danspec.danspec.user.domain.type.Campus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[\\w.-]+@dankook\\.ac\\.kr$", message = "단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 10, max = 20, message = "비밀번호는 10자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).+$", message = "비밀번호는 영문과 숫자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordConfirm;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotNull(message = "캠퍼스를 선택해주세요.")
    private Campus campus;

    private String college;
    private String major;
    private String grade;
    private String interestJobPrimary;
    private String interestJobSecondary;
    private String interestJobTertiary;
    private String tagline;
}
