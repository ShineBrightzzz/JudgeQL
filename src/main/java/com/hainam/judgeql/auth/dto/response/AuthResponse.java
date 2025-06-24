package com.hainam.judgeql.auth.dto.response;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private UUID id;
    private String username;
    private String givenName;
    private String email;
    private String familyName;
    private String pictureUrl;
    private Boolean emailVerified;
    private Boolean isAdmin;
    private UUID roleId;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
