package com.danspec.danspec.user.service;

import com.danspec.danspec.common.exception.DanspecException;
import com.danspec.danspec.common.exception.ErrorCode;
import com.danspec.danspec.user.domain.User;
import com.danspec.danspec.user.dto.UserResponse;
import com.danspec.danspec.user.dto.UserUpdateRequest;
import com.danspec.danspec.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DanspecException(ErrorCode.EMAIL_NOT_FOUND));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateMyProfile(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DanspecException(ErrorCode.EMAIL_NOT_FOUND));

        user.update(
                request.getName(),
                request.getCampus(),
                request.getCollege(),
                request.getMajor(),
                request.getGrade(),
                request.getInterestJobPrimary(),
                request.getInterestJobSecondary(),
                request.getInterestJobTertiary(),
                request.getTagline()
        );

        return UserResponse.from(userRepository.save(user));
    }
}

