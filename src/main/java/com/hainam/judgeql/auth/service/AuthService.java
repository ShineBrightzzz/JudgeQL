package com.hainam.judgeql.auth.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hainam.judgeql.auth.domain.RefreshToken;
import com.hainam.judgeql.auth.dto.request.*;
import com.hainam.judgeql.auth.dto.response.AuthResponse;
import com.hainam.judgeql.auth.exception.EmailAlreadyExistsException;
import com.hainam.judgeql.auth.exception.EmailNotFoundException;
import com.hainam.judgeql.auth.mapper.AuthMapper;
import com.hainam.judgeql.shared.security.InvalidPasswordException;
import com.hainam.judgeql.user.domain.User;
import com.hainam.judgeql.user.service.UserService;
import com.hainam.judgeql.auth.util.JwtTokenProvider;

@Service
public class AuthService {    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, 
            JwtTokenProvider jwtTokenProvider, RefreshTokenService refreshTokenService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }    
    
    
    @Transactional
    public AuthResponse login(LoginRequest loginRequest){
        User user = userService.getUserByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new EmailNotFoundException());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtTokenProvider.generateToken(user);
        
        // Create new refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        
        AuthResponse response = AuthMapper.toAuthResponse(user);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        return response;
    }    
    
    
    @Transactional
    public AuthResponse register(RegisterRequest registerRequest){
        if(userService.existsByEmail(registerRequest.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        User user = AuthMapper.toEntity(registerRequest, passwordEncoder);
        User savedUser = userService.save(user);
        
        String accessToken = jwtTokenProvider.generateToken(savedUser);
        
        // Revoke all existing refresh tokens
        refreshTokenService.revokeAllUserTokens(savedUser);
        
        // Create new refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser);
        
        AuthResponse response = AuthMapper.toAuthResponse(savedUser);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken());
        return response;
    }    
    
    @Transactional
    public AuthResponse refresh(RefreshTokenRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();
        
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        refreshTokenService.verifyExpiration(refreshToken);
        
        User user = refreshToken.getUser();
        String newAccessToken = jwtTokenProvider.generateToken(user);
        
        // Revoke old refresh token and create new one
        refreshTokenService.revokeToken(requestRefreshToken);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        
        AuthResponse response = AuthMapper.toAuthResponse(user);
        response.setAccessToken(newAccessToken);
        response.setRefreshToken(newRefreshToken.getToken());
        return response;
    }
    
    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        String refreshToken = logoutRequest.getRefreshToken();
        refreshTokenService.revokeToken(refreshToken);
    }
}
