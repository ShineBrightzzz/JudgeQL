package com.hainam.judgeql.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hainam.judgeql.auth.dto.request.LoginRequest;
import com.hainam.judgeql.auth.dto.request.RefreshTokenRequest;
import com.hainam.judgeql.auth.dto.request.RegisterRequest;
import com.hainam.judgeql.auth.dto.response.AuthResponse;
import com.hainam.judgeql.auth.service.AuthService;
import com.hainam.judgeql.shared.response.ApiResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest loginRequest){
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response, "Đăng nhập thành công"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequest registerRequest){
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Đăng kí thành công"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(@RequestBody RefreshTokenRequest refreshRequest) {
        AuthResponse response = authService.refresh(refreshRequest);
        return ResponseEntity
                .ok()
                .body(ApiResponse.success(response, "Refresh token thành công"));
    }
}
