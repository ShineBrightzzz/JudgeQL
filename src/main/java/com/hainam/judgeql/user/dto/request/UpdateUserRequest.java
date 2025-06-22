package com.hainam.judgeql.user.dto.request;

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
public class UpdateUserRequest {
    private String username;
    private String givenName;
    private String email;
    private String familyName;
    private String password;
    private String pictureUrl;
    private Boolean emailVerified;
    private Boolean isAdmin;
    private UUID roleId;
}
