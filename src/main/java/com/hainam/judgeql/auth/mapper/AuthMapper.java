package com.hainam.judgeql.auth.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.hainam.judgeql.auth.dto.request.RegisterRequest;
import com.hainam.judgeql.auth.dto.response.AuthResponse;
import com.hainam.judgeql.user.domain.User;

public class AuthMapper {
    public static AuthResponse toAuthResponse(User user) {
        return AuthResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .givenName(user.getGivenName())
                .email(user.getEmail())
                .familyName(user.getFamilyName())
                .pictureUrl(user.getPictureUrl())
                .emailVerified(user.getEmailVerified())
                .isAdmin(user.getIsAdmin())
                .roleId(user.getRoleId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }

    public static User toEntity(RegisterRequest registerRequest, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .givenName(registerRequest.getGivenName())
                .familyName(registerRequest.getFamilyName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
    }
}
