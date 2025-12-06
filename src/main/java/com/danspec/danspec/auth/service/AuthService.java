package com.danspec.danspec.auth.service;

import com.danspec.danspec.auth.domain.EmailVerification;
import com.danspec.danspec.auth.domain.RefreshToken;
import com.danspec.danspec.auth.dto.*;
import com.danspec.danspec.auth.jwt.JwtTokenProvider;
import com.danspec.danspec.auth.repository.EmailVerificationRepository;
import com.danspec.danspec.auth.repository.RefreshTokenRepository;
import com.danspec.danspec.common.exception.DanspecException;
import com.danspec.danspec.common.exception.ErrorCode;
import com.danspec.danspec.config.JwtProperties;
import com.danspec.danspec.mail.service.MailService;
import com.danspec.danspec.user.domain.User;
import com.danspec.danspec.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;

    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRY_MINUTES = 5;

    @Transactional
    public void requestEmailVerification(EmailRequest request) {
        String email = request.getEmail();

        // 단국대학교 이메일 도메인 검증
        if (!email.endsWith("@dankook.ac.kr")) {
            throw new DanspecException(ErrorCode.BAD_REQUEST, "단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.");
        }

        // 이미 가입된 이메일인지 확인
        if (userRepository.existsByEmail(email)) {
            throw new DanspecException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 인증코드 생성
        String code = generateVerificationCode();

        // 기존 인증 정보가 있으면 업데이트, 없으면 새로 생성
        Optional<EmailVerification> existing = emailVerificationRepository.findByEmail(email);
        EmailVerification verification;
        
        if (existing.isPresent()) {
            verification = existing.get();
            verification.updateCode(code, LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES));
        } else {
            verification = EmailVerification.builder()
                    .email(email)
                    .code(code)
                    .expiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRY_MINUTES))
                    .verified(false)
                    .build();
        }

        emailVerificationRepository.save(verification);

        // 이메일 발송
        mailService.sendVerificationCode(email, code);
    }

    @Transactional
    public void verifyEmail(EmailVerifyRequest request) {
        EmailVerification verification = emailVerificationRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DanspecException(ErrorCode.VERIFICATION_NOT_FOUND));

        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new DanspecException(ErrorCode.VERIFICATION_CODE_EXPIRED);
        }

        if (!verification.getCode().equals(request.getCode())) {
            throw new DanspecException(ErrorCode.VERIFICATION_CODE_INVALID);
        }

        verification.verify();
        emailVerificationRepository.save(verification);
    }

    @Transactional
    public void signup(SignupRequest request) {
        String email = request.getEmail();

        // 단국대학교 이메일 도메인 검증
        if (!email.endsWith("@dankook.ac.kr")) {
            throw new DanspecException(ErrorCode.BAD_REQUEST, "단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.");
        }

        // 비밀번호 확인
        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new DanspecException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 길이 검증 (10-20자)
        String password = request.getPassword();
        if (password.length() < 10 || password.length() > 20) {
            throw new DanspecException(ErrorCode.BAD_REQUEST, "비밀번호는 10자 이상 20자 이하여야 합니다.");
        }

        // 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new DanspecException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 이메일 인증 확인
        EmailVerification verification = emailVerificationRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new DanspecException(ErrorCode.EMAIL_NOT_VERIFIED));

        if (!verification.getVerified()) {
            throw new DanspecException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .campus(request.getCampus())
                .college(request.getCollege())
                .major(request.getMajor())
                .grade(request.getGrade())
                .interestJobPrimary(request.getInterestJobPrimary())
                .interestJobSecondary(request.getInterestJobSecondary())
                .interestJobTertiary(request.getInterestJobTertiary())
                .tagline(request.getTagline())
                .build();

        userRepository.save(user);

        // 인증 정보 삭제
        emailVerificationRepository.delete(verification);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        String email = request.getEmail();

        // 단국대학교 이메일 도메인 검증
        if (!email.endsWith("@dankook.ac.kr")) {
            throw new DanspecException(ErrorCode.BAD_REQUEST, "단국대학교 이메일(@dankook.ac.kr)만 사용 가능합니다.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        String authenticatedEmail = authentication.getName();

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authenticatedEmail);
        String refreshToken = jwtTokenProvider.createRefreshToken(authenticatedEmail);

        // 리프레시 토큰 저장
        Optional<RefreshToken> existing = refreshTokenRepository.findByEmail(authenticatedEmail);
        RefreshToken token;
        
        if (existing.isPresent()) {
            token = existing.get();
            token.updateExpiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshExpiration() / 1000));
        } else {
            token = RefreshToken.builder()
                    .token(refreshToken)
                    .email(authenticatedEmail)
                    .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshExpiration() / 1000))
                    .build();
        }

        refreshTokenRepository.save(token);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpiration())
                .build();
    }

    @Transactional
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new DanspecException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new DanspecException(ErrorCode.TOKEN_EXPIRED);
        }

        String email = refreshToken.getEmail();
        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        String newRefreshTokenValue = jwtTokenProvider.createRefreshToken(email);

        refreshToken.updateToken(newRefreshTokenValue, LocalDateTime.now().plusSeconds(jwtProperties.getRefreshExpiration() / 1000));
        refreshTokenRepository.save(refreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(jwtProperties.getExpiration())
                .build();
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        String email = request.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DanspecException(ErrorCode.EMAIL_NOT_FOUND));

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new DanspecException(ErrorCode.INVALID_CREDENTIALS, "현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 일치/길이 검증
        String newPassword = request.getNewPassword();
        if (!newPassword.equals(request.getNewPasswordConfirm())) {
            throw new DanspecException(ErrorCode.BAD_REQUEST, "새 비밀번호가 일치하지 않습니다.");
        }
        if (newPassword.length() < 10 || newPassword.length() > 20) {
            throw new DanspecException(ErrorCode.BAD_REQUEST, "비밀번호는 10자 이상 20자 이하여야 합니다.");
        }

        // 비밀번호 변경
        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 비밀번호 변경 시 기존 리프레시 토큰 무효화
        refreshTokenRepository.deleteByEmail(email);
    }

    @Transactional
    public void logout(LogoutRequest request) {
        String email = request.getEmail();

        if (!userRepository.existsByEmail(email)) {
            throw new DanspecException(ErrorCode.EMAIL_NOT_FOUND);
        }

        refreshTokenRepository.deleteByEmail(email);
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
