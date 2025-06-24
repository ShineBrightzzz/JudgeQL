package com.hainam.judgeql.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(LoginRequest loginRequest){
        User user = userService.getUserByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new EmailNotFoundException());

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String accessToken = jwtTokenProvider.generateToken(user);
        AuthResponse response = AuthMapper.toAuthResponse(user);
        response.setAccessToken(accessToken);
        return response;
    }

    public AuthResponse register(RegisterRequest registerRequest){
        if(userService.existsByEmail(registerRequest.getEmail())){
            throw new EmailAlreadyExistsException();
        }

        User user = AuthMapper.toEntity(registerRequest, passwordEncoder);
        User savedUser = userService.save(user);
        return AuthMapper.toAuthResponse(savedUser);
    }
}
