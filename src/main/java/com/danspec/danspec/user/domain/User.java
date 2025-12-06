package com.danspec.danspec.user.domain;

import com.danspec.danspec.user.domain.type.Campus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Campus campus;

    @Column(length = 50)
    private String college;

    @Column(length = 50)
    private String major;

    @Column(length = 10)
    private String grade;

    @Column(name = "interest_job_primary", length = 50)
    private String interestJobPrimary;

    @Column(name = "interest_job_secondary", length = 50)
    private String interestJobSecondary;

    @Column(name = "interest_job_tertiary", length = 50)
    private String interestJobTertiary;

    @Column(length = 200)
    private String tagline;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String name, Campus campus, String college,
                String major, String grade, String interestJobPrimary, String interestJobSecondary,
                String interestJobTertiary, String tagline) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.campus = campus;
        this.college = college;
        this.major = major;
        this.grade = grade;
        this.interestJobPrimary = interestJobPrimary;
        this.interestJobSecondary = interestJobSecondary;
        this.interestJobTertiary = interestJobTertiary;
        this.tagline = tagline;
    }

    public void update(String name, Campus campus, String college, String major, String grade,
                       String interestJobPrimary, String interestJobSecondary, String interestJobTertiary,
                       String tagline) {
        this.name = name;
        this.campus = campus;
        this.college = college;
        this.major = major;
        this.grade = grade;
        this.interestJobPrimary = interestJobPrimary;
        this.interestJobSecondary = interestJobSecondary;
        this.interestJobTertiary = interestJobTertiary;
        this.tagline = tagline;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
