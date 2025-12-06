package com.danspec.danspec.user.controller;

import com.danspec.danspec.common.dto.ApiResponse;
import com.danspec.danspec.user.dto.UserResponse;
import com.danspec.danspec.user.dto.UserUpdateRequest;
import com.danspec.danspec.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyProfile() {
        String email = getCurrentUserEmail();
        UserResponse response = userService.getMyProfile(email);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMyProfile(
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        String email = getCurrentUserEmail();
        UserResponse response = userService.updateMyProfile(email, updateRequest);
        return ResponseEntity.ok(ApiResponse.success("회원정보가 수정되었습니다.", response));
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("인증된 사용자를 찾을 수 없습니다.");
    }
}

