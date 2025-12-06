package com.danspec.danspec.user.dto;

import com.danspec.danspec.user.domain.User;
import com.danspec.danspec.user.domain.type.Campus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private Campus campus;
    private String college;
    private String major;
    private String grade;
    private String interestJobPrimary;
    private String interestJobSecondary;
    private String interestJobTertiary;
    private String tagline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .campus(user.getCampus())
                .college(user.getCollege())
                .major(user.getMajor())
                .grade(user.getGrade())
                .interestJobPrimary(user.getInterestJobPrimary())
                .interestJobSecondary(user.getInterestJobSecondary())
                .interestJobTertiary(user.getInterestJobTertiary())
                .tagline(user.getTagline())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

