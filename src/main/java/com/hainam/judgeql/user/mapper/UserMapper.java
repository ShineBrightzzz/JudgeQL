package com.hainam.judgeql.user.mapper;

import com.hainam.judgeql.user.domain.User;
import com.hainam.judgeql.user.dto.request.CreateUserRequest;
import com.hainam.judgeql.user.dto.response.UserResponse;

public class UserMapper {
    public static User toEntity(CreateUserRequest request){
        return User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(request.getPassword())
            .givenName(request.getGivenName())
            .familyName(request.getFamilyName())
            .build();
    }

    public static UserResponse toResponse(User user){
        return UserResponse.builder()
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

    public static void updateEntity(User user, CreateUserRequest request) {
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setGivenName(request.getGivenName());
        user.setFamilyName(request.getFamilyName());
    }
}
