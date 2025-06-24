package com.hainam.judgeql.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hainam.judgeql.auth.dto.request.*;
import com.hainam.judgeql.auth.dto.response.AuthResponse;
import com.hainam.judgeql.auth.exception.EmailAlreadyExistsException;
import com.hainam.judgeql.auth.mapper.AuthMapper;
import com.hainam.judgeql.user.domain.User;
import com.hainam.judgeql.user.service.UserService;

@Service
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void login(LoginRequest loginRequest){
        
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
