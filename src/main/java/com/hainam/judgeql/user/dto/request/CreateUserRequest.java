package com.hainam.judgeql.user.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String givenName;

    @NotBlank 
    private String familyName;

    @NotBlank
    private String password;
}
